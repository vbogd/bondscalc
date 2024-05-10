package com.nxtru.bondscalc.domain.usecase

import com.nxtru.bondscalc.domain.util.asLocalDate
import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

internal class CalcTest {

    @Test
    fun getIncomeTest1() {
        assertDoubleEquals(
            15.69,
            getIncome(
                0.05 / 100, 0.13, 0.01 / 100, 1000.0,
                97.10 / 100, date("26.03.2021"),
                99.0 / 100, date("06.05.2021"), false
            )
        )
    }

    @Test
    fun getProfitabilityTest1() {
        assertDoubleEquals(
            14.37,
            getProfitability(
                0.05 / 100, 0.13, 0.01 / 100, 1000.0,
                97.10 / 100, date("26.03.2021"),
                99.0 / 100, date("06.05.2021"), false
            )
        )
    }

    @Test
    fun getIncomeTest2() {
        assertDoubleEquals(
            78.51,
            getIncome(
                0.04 / 100, 0.13, 0.0 / 100, 1000.0,
                90.90 / 100, date("22.07.2022"),
                100.0 / 100, date("27.05.2023"), false
            )
        )
    }

    @Test
    fun getProfitabilityTest2() {
        assertDoubleEquals(
            10.10,
            getProfitability(
                0.04 / 100, 0.13, 0.0 / 100, 1000.0,
                90.90 / 100, date("19.07.2022"),
                100.0 / 100, date("27.05.2023"), false
            )
        )
    }

    @Test
    fun getIncomeTest3() {
        assertDoubleEquals(
            62.35,
            getIncome(
                0.07 / 100, 0.13, 8.65 / 100, 1000.0,
                99.85 / 100, date("05.10.2021"),
                100.0 / 100, date("03.08.2022"), false
            )
        )
    }

    @Test
    fun getProfitabilityTest3() {
        assertDoubleEquals(
            7.54,
            getProfitability(
                0.07 / 100, 0.13, 8.65 / 100, 1000.0,
                99.85 / 100, date("05.10.2021"),
                100.0 / 100, date("03.08.2022"), false
            )
        )
    }

    @Test
    fun getIncomeTest4() {
        assertDoubleEquals(
            62.96,
            getIncome(
                0.07 / 100, 0.13, 8.65 / 100, 1000.0,
                99.85 / 100, date("05.10.2021"),
                100.0 / 100, date("03.08.2022"), true
            )
        )
    }

    @Test
    fun getProfitabilityTest4() {
        assertDoubleEquals(
            7.61,
            getProfitability(
                0.07 / 100, 0.13, 8.65 / 100, 1000.0,
                99.85 / 100, date("05.10.2021"),
                100.0 / 100, date("03.08.2022"), true
            )
        )
    }

    private fun date(date: String) = date.asLocalDate()!!
}

internal fun assertDoubleEquals(expected: Double, actual: Double, precision: Int = 2) {
    assertEquals(
        String.format("%,.${precision}f", expected),
        String.format("%,.${precision}f", actual)
    )
}
