package com.nxtru.bondscalc.data.bondinfo.moex

import com.nxtru.bondscalc.domain.models.BriefBondInfo
import org.junit.Assert.*

import org.junit.Test

class MoexServiceTest {

//    @Test
//    fun searchBonds() {
//        val subj = MoexService()
//        measureTimeMillis {
//            runBlocking {
//                val resp = subj.searchBonds("офз 29").joinToString("\n")
//                println(">>>\n$resp")
//            }
//        }
//    }

    @Test
    fun extractTickerTest() {
        assertEquals(
            BriefBondInfo("ВТБ Б1-311", "RU000A104ZQ9"),
            extractTicker(
            "419325623;RU000A104ZQ9;ВТБ Б1-311;4B02-311-01000-B-001P;Банк ВТБ (ПАО) Б-1-311;RU000A104ZQ9;1;1200;Банк ВТБ (публичное акционерное общество);7702070139;00032520;4B02-311-01000-B-001P;exchange_bond;stock_bonds;TQCB;TQCB"
            )
        )
        assertEquals(
            null,
            extractTicker("securities")
        )
        assertEquals(
            null,
            extractTicker("")
        )
    }

    @Test
    fun extractTickersTest() {
        val lines = readLines("/extractTickets/ofz_29.csv")
        assertEquals(
            listOf(
                BriefBondInfo("ОФЗ 29006", "RU000A0JV4L2"),
                BriefBondInfo("ОФЗ 29014", "RU000A101N52"),
            ),
            extractTickers("ОФЗ 29", lines)
        )
        assertEquals(
            listOf(
                BriefBondInfo("ОФЗ 26229", "RU000A100EG3"),
                BriefBondInfo("ОФЗ 29006", "RU000A0JV4L2"),
                BriefBondInfo("ОФЗ 29014", "RU000A101N52"),
            ),
            extractTickers("ОФЗ 2", lines)
        )
        // search by isin
        assertEquals(
            listOf(
                BriefBondInfo("ОФЗ 29006", "RU000A0JV4L2"),
            ),
            extractTickers("JV4L2", lines)
        )
        assertEquals(
            listOf<String>(),
            extractTickers("ОФЗ 299", lines)
        )
    }
}

fun readLines(resource: String): List<String> =
    object {}.javaClass.getResource(resource)?.readText()?.lines()
        ?: throw IllegalArgumentException("Resource not found: $resource")
