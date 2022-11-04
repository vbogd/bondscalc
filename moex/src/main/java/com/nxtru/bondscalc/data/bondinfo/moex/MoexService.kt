package com.nxtru.bondscalc.data.bondinfo.moex

import com.nxtru.bondscalc.domain.bondinfo.BondInfoService
import com.nxtru.bondscalc.domain.models.BriefBondInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.net.URLEncoder

private object MoexRoutes {
    private const val baseUrl = "https://iss.moex.com"

    fun searchBondsUrl(query: String) =
        "$baseUrl/iss/securities.csv?engine=stock&market=bonds&iss.meta=off&q=${encode(query)}"

    private fun encode(s: String) = URLEncoder.encode(s, "UTF-8")
}

// TODO: use HttpURLConnection? https://developer.android.com/reference/java/net/HttpURLConnection
class MoexService : BondInfoService {
    private val client: HttpClient = createHttpClient()

    // see http://iss.moex.com/iss/reference/5
    override suspend fun searchBonds(query: String): List<BriefBondInfo>? {
        try {
            // TODO: CSV response is in windows-1251 encoding
            val resp = client.get { url(MoexRoutes.searchBondsUrl(query)) }
            return if (resp.status == HttpStatusCode.OK)
                extractTickers(query, resp.body<String>().lines())
            else
                emptyList()
        } catch (e: Exception) {
            // TODO: add logging
            return null
        }
    }
}

// based on https://www.section.io/engineering-education/making-http-requests-with-ktor-in-android/
private fun createHttpClient(): HttpClient {
    return HttpClient(Android) {
        // Logging
//        install(Logging) {
//            level = LogLevel.ALL
//        }
        // JSON
//        install(JsonFeature) {
//            serializer = KotlinxSerializer(json)
//            //or serializer = KotlinxSerializer()
//        }
        // Timeout
        install(HttpTimeout) {
            requestTimeoutMillis = 15000L
            connectTimeoutMillis = 15000L
            socketTimeoutMillis = 15000L
        }
        // Apply to all requests
        defaultRequest {
            // Parameter("api_key", "some_api_key")
            // Content Type
//            accept(ContentType.Application.Json)
        }
    }
}

internal fun extractTickers(query: String, csvLines: List<String>): List<BriefBondInfo> =
    csvLines
        .asSequence()
        .mapNotNull { extractTicker(it) }
        .filter {
            it.ticker.contains(query, ignoreCase = true) ||
                    it.isin.contains(query, ignoreCase = true)
        }
        .filter(BriefBondInfo::isTraded)
        .distinct()
        .sortedBy(BriefBondInfo::ticker)
        .toList()

internal fun extractTicker(csv: String): BriefBondInfo? {
    val limit = 8
    val fields = csv.split(";", ignoreCase = false, limit = limit)
    if (fields.size != limit) return null
    val ticker = fields[2]
    val isin = fields[5]
    val isTraded = fields[6] == "1"
    return BriefBondInfo(ticker, isin, isTraded)
}
