package com.nxtru.bondscalc.data.bondinfo.moex

import com.nxtru.bondscalc.domain.bondinfo.BondInfoRepository
import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.models.BriefBondInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder

private object MoexRoutes {
    private const val baseUrl = "https://iss.moex.com"

    fun searchBondsUrl(query: String) =
        "$baseUrl/iss/securities.csv?engine=stock&market=bonds&iss.meta=off&q=${encode(query)}"

    fun loadBondInfoUrl(isin: String) =
        "$baseUrl/iss/engines/stock/markets/bonds/securities/$isin.csv?iss.only=securities,marketdata&iss.meta=off&iss.df=%25d.%25m.%25Y"

    private fun encode(s: String) = URLEncoder.encode(s, "UTF-8")
}

// TODO: use HttpURLConnection? https://developer.android.com/reference/java/net/HttpURLConnection
class MoexRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BondInfoRepository {
    private val client: HttpClient = createHttpClient()

    // see http://iss.moex.com/iss/reference/5
    override suspend fun searchBonds(query: String): List<BriefBondInfo>? {
        return withContext(dispatcher) {
            try {
                // TODO: CSV response is in windows-1251 encoding
                val resp = client.get { url(MoexRoutes.searchBondsUrl(query)) }
                if (resp.status == HttpStatusCode.OK)
                    extractTickers(query, resp.body<String>().lines())
                else
                    emptyList()
            } catch (e: Exception) {
                // TODO: add logging
                null
            }
        }
    }

    override suspend fun loadBondInfo(secId: String): BondInfo? {
        return withContext(dispatcher) {
            try {
                // TODO: CSV response is in windows-1251 encoding
                val resp = client.get { url(MoexRoutes.loadBondInfoUrl(secId)) }
                if (resp.status == HttpStatusCode.OK) {
                    extractBondInfo(resp.body<String>().lines())
                } else
                    null
            } catch (e: Exception) {
                // TODO: add logging
                null
            }
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
    val fields = csv.split(";", limit = limit)
    if (fields.size != limit) return null
    return BriefBondInfo(
        secId = fields[1],
        ticker = fields[2],
        isin = fields[5],
        isTraded = fields[6] == "1"
    )
}

internal fun extractBondInfo(csvLines: List<String>): BondInfo? {
    return extractSecuritiesSectionInfo(csvLines)?.let {
        extractMarketDataSectionInfo(it, csvLines)
    }
}

// valid MOEX bonds boards
private val validBoards = listOf("TQCB", "TQOB")

// extract info from "securities" section of MOEX response
private fun extractSecuritiesSectionInfo(csvLines: List<String>): BondInfo? {
    // actual size of parts in this section is 40
    val limit = 39
    return csvLines
        .map { it.split(";", limit = limit) }
        .filter { it.size == limit }
        .map { fields ->
            BondInfo(
                secId = fields[0],
                ticker = fields[2],
                isin = fields[29],
                parValue = fields[10],
                currencyId = fields[26],
                coupon = fields[36],
                couponPeriod = fields[15],
                nextCouponDate = fields[6],
                maturityDate = fields[13],
                offerDate = fields[37],
                lastPrice = fields[8],
                boardId = fields[1],
            )
        }
        .firstOrNull { it.boardId in validBoards }
}

private fun extractMarketDataSectionInfo(bondInfo: BondInfo, csvLines: List<String>): BondInfo {
    // actual size of parts in this section is 60
    val limit = 45
    val parts = csvLines
        .map { it.split(";", limit = limit) }
        .filter { it.size == limit }
        .firstOrNull { it[0] == bondInfo.secId && it[33] in validBoards }

    if (parts != null) {
        val lastPrice = parts[11]
        var newBondInfo = bondInfo
        if (lastPrice.isNotEmpty()) {
            newBondInfo = bondInfo.copy(
                lastPrice = lastPrice
            )
        }
        return newBondInfo
    } else {
        return bondInfo
    }
}
