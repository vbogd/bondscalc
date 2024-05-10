package com.nxtru.bondscalc.domain.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

fun String.asLocalDate(): LocalDate? {
    if (length != 10) return null
    return LocalDate.parse(this, formatter)
}

fun LocalDate.asString() = formatter.format(this)