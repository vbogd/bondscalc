package com.nxtru.bondscalc.data.bondinfo.moex

import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.models.BriefBondInfo
import org.junit.Assert.*

import org.junit.Test

class MoexServiceTest {

//    @Test
//    fun searchBonds() {
//        val subj = MoexService()
//        kotlinx.coroutines.runBlocking {
//            val resp = subj.searchBonds("офз 29")?.map(BriefBondInfo::ticker)?.joinToString("\n")
//            println(">>>\n$resp")
//        }
//    }
//
//    @Test
//    fun loadBondInfo() {
//        val subj = MoexService()
//        kotlinx.coroutines.runBlocking {
//            val resp = subj.loadBondInfo("RU000A100EG3")
//            println(">>>\n$resp")
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

    @Test
    fun extreactBondInfoTest() {
        assertEquals(
            BondInfo(ticker="ВТБ Б1-311", isin="RU000A104ZQ9", parValue="1000", coupon="7.500", maturityDate="27.07.2023"),
            extreactBondInfo(readLines("/extractTickets/RU000A104ZQ9.csv"))
        )
        assertEquals(
            BondInfo(ticker="ГТЛК 1P-06", isin="RU000A0ZYAP9", parValue="755.55", coupon="9.500", maturityDate="01.09.2032"),
            extreactBondInfo(readLines("/extractTickets/RU000A0ZYAP9.csv"))
        )
        assertEquals(
            null,
            extreactBondInfo(listOf("RU000A104ZQ9;TQCB;ВТБ Б1-311"))
        )
    }
}

private fun readLines(resource: String): List<String> =
    object {}.javaClass.getResource(resource)?.readText()?.lines()
        ?: throw IllegalArgumentException("Resource not found: $resource")
