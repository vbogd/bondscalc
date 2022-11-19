package com.nxtru.bondscalc.domain.usecase

import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.domain.models.BondCalcResult
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BondCalcUseCase {
    fun execute(params: BondParams): BondCalcResult? {
        try {
            val commission = getPercentValue(params.commission)
            val tax = getPercentValue(params.tax)
            val coupon = getPercentValue(params.coupon)
            val parValue = getDoubleValue(params.parValue)
            val buyPrice = getPercentValue(params.buyPrice)
            val buyDate = getDateValue(params.buyDate)
            val sellPrice = getPercentValue(params.sellPrice)
            val sellDate = getDateValue(params.sellDate)
            val tillMaturity = params.tillMaturity

            if (commission == null) return null
            if (tax == null) return null
            if (coupon == null) return null
            if (parValue == null) return null
            if (buyPrice == null) return null
            if (buyDate == null) return null
            if (sellPrice == null) return null
            if (sellDate == null) return null

            val income = getIncome(
                commission, tax, coupon, parValue, buyPrice, buyDate, sellPrice, sellDate, tillMaturity
            )
            val ytm = getProfitability(
                commission, tax, coupon, parValue, buyPrice, buyDate, sellPrice, sellDate, tillMaturity)
            val currentYield = calculateCurrentYield(tax, coupon, buyPrice)

            return BondCalcResult(income, ytm, currentYield)
        } catch (e: NumberFormatException) {
            return null
        } catch (e: ParseException) {
            return null
        }
    }
}

private fun getPercentValue(value: String) = if (value == "") null else toDouble(value) / 100

private fun getDoubleValue(value: String) = if (value == "") null else toDouble(value)

private fun getDateValue(value: String): Date? {
    if (value.length != 10) return null
    val format = SimpleDateFormat("dd.MM.yyyy")
    return format.parse(value)
}

private fun toDouble(value: String): Double = value.replace(',', '.').toDouble()

private fun calculateCurrentYield(
    tax: Double,
    coupon: Double,
    buyPrice: Double,

): Double {
    return coupon / buyPrice * 100 * (1 - tax)
}
