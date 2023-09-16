package com.nxtru.bondscalc.data.bondinfo.moex

import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.models.BriefBondInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MoexRepositoryTest {

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
            BriefBondInfo("RU000A104ZQ9", "ВТБ Б1-311", "RU000A104ZQ9"),
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
                BriefBondInfo("SU29006RMFS2", "ОФЗ 29006", "RU000A0JV4L2"),
                BriefBondInfo("SU29014RMFS6", "ОФЗ 29014", "RU000A101N52"),
            ),
            extractTickers("ОФЗ 29", lines)
        )
        assertEquals(
            listOf(
                BriefBondInfo("SU26229RMFS3", "ОФЗ 26229", "RU000A100EG3"),
                BriefBondInfo("SU29006RMFS2", "ОФЗ 29006", "RU000A0JV4L2"),
                BriefBondInfo("SU29014RMFS6", "ОФЗ 29014", "RU000A101N52"),
            ),
            extractTickers("ОФЗ 2", lines)
        )
        // search by isin
        assertEquals(
            listOf(
                BriefBondInfo("SU29006RMFS2", "ОФЗ 29006", "RU000A0JV4L2"),
            ),
            extractTickers("JV4L2", lines)
        )
        assertEquals(
            emptyList<BriefBondInfo>(),
            extractTickers("ОФЗ 299", lines)
        )
    }

    @Test
    fun extreactBondInfoTest() {
        assertEquals(
            BondInfo(
                secId = "RU000A104ZQ9",
                ticker = "ВТБ Б1-311",
                isin = "RU000A104ZQ9",
                parValue = "1000",
                currencyId = "SUR",
                coupon = "7.500",
                couponPeriod = "28",
                nextCouponDate = "27.03.2023",
                maturityDate = "27.07.2023",
                offerDate = "",
                lastPrice = "99.94",
                boardId = "TQCB",
            ),
            extractBondInfo(readLines("/extractTickets/RU000A104ZQ9.csv"))
        )
        assertEquals(
            BondInfo(
                secId = "RU000A0ZYAP9",
                ticker = "ГТЛК 1P-06",
                isin = "RU000A0ZYAP9",
                parValue = "728.9",
                currencyId = "SUR",
                coupon = "9.000",
                couponPeriod = "91",
                nextCouponDate = "14.06.2023",
                maturityDate = "01.09.2032",
                offerDate = "",
                lastPrice = "93.11",
                boardId = "TQCB",
            ),
            extractBondInfo(readLines("/extractTickets/RU000A0ZYAP9.csv"))
        )
        assertEquals(
            BondInfo(
                secId = "SU26229RMFS3",
                ticker = "ОФЗ 26229",
                isin = "RU000A100EG3",
                parValue = "1000",
                currencyId = "SUR",
                coupon = "7.150",
                couponPeriod = "182",
                nextCouponDate = "17.05.2023",
                maturityDate = "12.11.2025",
                offerDate = "",
                lastPrice = "96.899",
                boardId = "TQOB",
            ),
            extractBondInfo(readLines("/extractTickets/SU26229RMFS3.csv"))
        )
        assertEquals(
            BondInfo(
                secId = "RU000A101J58",
                ticker = "СберИОС233",
                isin = "RU000A101J58",
                parValue = "1000",
                coupon = "0.010",
                couponPeriod = "1832",
                nextCouponDate = "01.04.2025",
                maturityDate = "01.04.2025",
                offerDate = "",
                currencyId = "SUR",
                lastPrice = "96.8",
                boardId = "TQCB",
            ),
            extractBondInfo(readLines("/extractTickets/RU000A101J58.csv"))
        )
        assertEquals(
            BondInfo(
                secId = "RU000A0ZZY42",
                ticker = "СолЛизБО03",
                isin = "RU000A0ZZY42",
                parValue = "1000",
                coupon = "12.500",
                couponPeriod = "91",
                nextCouponDate = "11.12.2023",
                maturityDate = "11.12.2023",
                offerDate = "",
                currencyId = "SUR",
                lastPrice = "100",
                boardId = "TQIR",
            ),
            extractBondInfo(readLines("/extractTickets/RU000A0ZZY42.csv"))
        )
        assertEquals(
            null,
            extractBondInfo(listOf("RU000A104ZQ9;TQCB;ВТБ Б1-311"))
        )
    }

    @Test
    fun `extreactBondInfo with offerDate`() {
        assertEquals(
            BondInfo(
                secId = "RU000A103117",
                ticker = "МВ ФИН 1Р1",
                isin = "RU000A103117",
                parValue = "1000",
                coupon = "7.300",
                couponPeriod = "182",
                nextCouponDate = "20.04.2023",
                maturityDate = "18.04.2024",
                offerDate = "25.04.2023",
                currencyId = "SUR",
                lastPrice = "99.4",
                boardId = "TQCB",
            ),
            extractBondInfo(readLines("/extractTickets/RU000A103117.csv"))
        )
    }
}

private fun readLines(resource: String): List<String> =
    object {}.javaClass.getResource(resource)?.readText()?.lines()
        ?: throw IllegalArgumentException("Resource not found: $resource")
