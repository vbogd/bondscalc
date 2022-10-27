package com.nxtru.bondscalc.domain.usecase

import com.nxtru.bondscalc.domain.models.BondParams

import org.junit.Assert.*
import org.junit.Test

class BondCalcUseCaseTest {

    private val subj = BondCalcUseCase()

    @Test
    fun `normal params`() {
        val actual = subj.execute(BondParams(
            "", "0.05", "13", "0.01", "1000",
            "97.10", "26.03.2021",
            "99", "06.05.2021", false
        ))

        assertNotNull("calculated", actual)
        assertDoubleEquals(15.69, actual!!.income)
        assertDoubleEquals(0.1437, actual.ytm, 4)
    }

    @Test
    fun `params with comma`() {
        val actual = subj.execute(BondParams(
            "", "0,05", "13,", "0.01", "1000,0",
            "97.10", "26.03.2021",
            "99", "06.05.2021", false
        ))

        assertNotNull("calculated", actual)
        assertDoubleEquals(15.69, actual!!.income)
        assertDoubleEquals(0.1437, actual.ytm, 4)
    }

    @Test
    fun `incomplete params`() {
        val actual = subj.execute(BondParams(
            "", "0,05", "", "0.01", "1000,0",
            "97.10", "26.03.2021",
            "99", "06.05.2021", false
        ))

        assertNull("not calculated", actual)
    }

    @Test
    fun `invalid commission`() {
        val actual = subj.execute(BondParams(
            "", "0,05,", "", "0.01", "1000,0",
            "97.10", "26.03.2021",
            "99", "06.05.2021", false
        ))

        assertNull("not calculated", actual)
    }

    @Test
    fun `invalid date`() {
        val actual = subj.execute(BondParams(
            "", "0,05,", "", "0.01", "1000,0",
            "97.10", "26.03",
            "99", "06.05.2021", false
        ))

        assertNull("not calculated", actual)
    }
}
