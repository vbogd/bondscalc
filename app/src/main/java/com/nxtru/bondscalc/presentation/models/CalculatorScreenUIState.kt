package com.nxtru.bondscalc.presentation.models

import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.presentation.BondCalcUIResult
import com.nxtru.bondscalc.presentation.getTodayDate

data class CalculatorScreenUIState(
    val bondParams: BondParams = BondParams.EMPTY,
    val calcResult: BondCalcUIResult = BondCalcUIResult.UNDEFINED,
    val bondInfo: BondInfo? = null,
) {

    private val offerDate = bondInfo?.offerDate ?: ""
    val hasOfferDate = bondInfo != null && bondInfo.offerDate != ""
    val tillOffer = bondInfo?.offerDate == bondParams.sellDate
    val tillMaturity = bondParams.tillMaturity

    fun setTillOffer() = copy(
        bondParams = bondParams.copy(
            sellDate = offerDate,
            sellPrice = "100",
            tillMaturity = false
        )
    )

    fun setTillMaturity(value: Boolean): CalculatorScreenUIState {
        val moexMaturityDate = bondInfo?.maturityDate ?: ""
        val newSellDate = if (moexMaturityDate != "" && value) moexMaturityDate else bondParams.sellDate
        return copy(
            bondParams = bondParams.copy(
                sellDate = newSellDate,
                tillMaturity = value,
                sellPrice = if (value) "100" else bondParams.sellPrice
            )
        )
    }

    fun update(value: BondInfo?): CalculatorScreenUIState {
        if (value == null) {
            return copy(
                bondInfo = null,
                bondParams = bondParams.copy(
                    ticker = "",
                    coupon = "",
                    parValue = "",
                    buyPrice = "",
                    buyDate = "",
                    sellPrice = "",
                    sellDate = "",
                )
            )
        }
        var newBondParams = bondParams.copy(
            ticker = value.ticker,
            coupon = value.coupon,
            parValue = value.parValue,
            buyPrice = value.lastPrice,
            buyDate = getTodayDate(),
        )
        newBondParams = if (value.offerDate != "") {
            newBondParams.setTillOffer(value.offerDate)
        } else {
            newBondParams.setTillMaturity(value.maturityDate)
        }
        return copy(
            bondInfo = value,
            bondParams = newBondParams
        )
    }
}

private fun BondParams.setTillOffer(offerDate: String) = copy(
    sellDate = offerDate,
    sellPrice = "100",
    tillMaturity = false,
)

private fun BondParams.setTillMaturity(maturityDate: String) = copy(
    sellDate = maturityDate,
    sellPrice = "100",
    tillMaturity = true,
)