package com.nxtru.bondscalc.domain.usecase

import java.util.*

//<string name="commission">комиссия, %</string>
//<string name="tax">налог, %</string>
//<string name="coupon">купон, %</string>
//<string name="par_value">номинал, руб.</string>
//<string name="buy">покупка</string>
//<string name="price">цена, %</string>
//<string name="date">дата</string>
//<string name="sell">продажа</string>

// returns income in roubles
internal fun getIncome(
    commission: Double, // C5
    tax: Double,        // C6
    coupon: Double,     // C7
    parValue: Double,   // C8
    buyPrice: Double,   // C10
    buyDate: Date,      // C11
    sellPrice: Double,  // C13
    sellDate: Date,     // C14
    tillMaturity: Boolean
): Double {
    val days = daysBetween(buyDate, sellDate)
    val commissionFixed = if (tillMaturity) commission / 2 else commission
    return (
            // ((C13*C8)-(C10*C8))
            (sellPrice * parValue - buyPrice * parValue) +
            // ((C8*C7)/365)*РАЗНДАТ(C11;C14;"d")
            parValue * coupon / 365 * days -
            // (((C13*C8)+(C10*C8))*C5)
            (sellPrice * parValue + buyPrice * parValue) * commissionFixed
            ) * (1 - tax)
// =((((C13*C8)-(C10*C8))+((C8*C7)/365)*РАЗНДАТ(C11;C14;"d"))-(((C13*C8)+(C10*C8))*C5))*(1-C6)
// =((((C14*C9)-(C11*C9))+((C9*C8)/365)*РАЗНДАТ(C12;C15;"d"))-(((C14*C9)+(C11*C9))*C6))*(1-C7))
// =((((C14*C9)-(C11*C9))+((C9*C8)/365)*РАЗНДАТ(C12;C15;"d"))-(((C14*C9)+(C11*C9))*C6/2))*(1-C7)
}

// returns income in percents per year
internal fun getProfitability(
    commission: Double, // C5
    tax: Double,        // C6
    coupon: Double,     // C7
    parValue: Double,   // C8
    buyPrice: Double,   // C10
    buyDate: Date,      // C11
    sellPrice: Double,  // C13
    sellDate: Date,     // C14
    tillMaturity: Boolean
): Double {
    val income = getIncome(
        commission, tax, coupon, parValue, buyPrice, buyDate, sellPrice, sellDate, tillMaturity
    )
    val days = daysBetween(buyDate, sellDate)

//    =((C16/(РАЗНДАТ(C11;C14;"d")))*365)/(C10*C8+((C13*C8)+(C10*C8))*C5)
    return (income * 365 / days) /
            (buyPrice * parValue + (sellPrice * parValue + buyPrice * parValue) * commission)
}

private fun daysBetween(start: Date, end: Date): Long =
    ((end.time - start.time) / (1000 * 3600 * 24))
