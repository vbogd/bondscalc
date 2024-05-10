package com.nxtru.bondscalc.domain.usecase

import com.nxtru.bondscalc.domain.models.BondParams

import org.junit.Assert.*
import org.junit.Test

class BondCalcUseCaseTest {

    private val subj = BondCalcUseCase()

    @Test
    fun `normal params`() {
        val actual = subj.execute(BondParams(
            ticker = "",
            commission = "0.05",
            tax = "13",
            coupon = "5",
            parValue = "1000",
            buyPrice = "97.10",
            buyDate = "26.03.2021",
            sellPrice = "99",
            sellDate = "06.05.2021",
            tillMaturity = false
        ))

        assertNotNull("calculated", actual)
        assertDoubleEquals(20.56, actual!!.income)
        assertDoubleEquals(18.83, actual.ytm)
        assertDoubleEquals(4.48, actual.currentYield)
        assertEquals(41, actual.days)
    }

    @Test
    fun `params with comma`() {
        val actual = subj.execute(BondParams(
            ticker = "",
            commission = "0,05",
            tax = "13,",
            coupon = "0.01",
            parValue = "1000,0",
            buyPrice = "97.10",
            buyDate = "26.03.2021",
            sellPrice = "99",
            sellDate = "06.05.2021",
            tillMaturity = false
        ))

        assertNotNull("calculated", actual)
        assertDoubleEquals(15.69, actual!!.income)
        assertDoubleEquals(14.37, actual.ytm)
        assertDoubleEquals(0.009, actual.currentYield, 4)
        assertEquals(41, actual.days)
    }

    @Test
    fun `incomplete params`() {
        val actual = subj.execute(BondParams(
            ticker = "",
            commission = "0,05",
            tax = "",
            coupon = "0.01",
            parValue = "1000,0",
            buyPrice = "97.10",
            buyDate = "26.03.2021",
            sellPrice = "99",
            sellDate = "06.05.2021",
            tillMaturity = false
        ))

        assertNull("not calculated", actual)
    }

    @Test
    fun `invalid commission`() {
        val actual = subj.execute(BondParams(
            ticker = "",
            commission = "0,05,",
            tax = "",
            coupon = "0.01",
            parValue = "1000,0",
            buyPrice = "97.10",
            buyDate = "26.03.2021",
            sellPrice = "99",
            sellDate = "06.05.2021",
            tillMaturity = false
        ))

        assertNull("not calculated", actual)
    }

    @Test
    fun `invalid date`() {
        val actual = subj.execute(BondParams(
            ticker = "",
            commission = "0,05,",
            tax = "",
            coupon = "0.01",
            parValue = "1000,0",
            buyPrice = "97.10",
            buyDate = "26.03",
            sellPrice = "99",
            sellDate = "06.05.2021",
            tillMaturity = false
        ))

        assertNull("not calculated", actual)
    }
}
