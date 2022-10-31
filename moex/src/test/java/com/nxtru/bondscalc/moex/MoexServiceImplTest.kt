package com.nxtru.bondscalc.moex

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test
import kotlin.system.measureTimeMillis

class MoexServiceImplTest {

    @Test
    fun searchBonds() {
        val subj = MoexServiceImpl()
        measureTimeMillis {
            runBlocking {
                val resp = subj.searchBonds("офз 29").joinToString("\n")
                println(">>>\n$resp")
            }
        }
    }

    @Test
    fun extractTickerTest() {
        assertEquals(
            "ВТБ Б1-311",
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
            listOf("ОФЗ 29006", "ОФЗ 29014"),
            extractTickers("ОФЗ 29", lines)
        )
        assertEquals(
            listOf("ОФЗ 26229", "ОФЗ 29006", "ОФЗ 29014"),
            extractTickers("ОФЗ 2", lines)
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
