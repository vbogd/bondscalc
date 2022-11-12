package com.nxtru.bondscalc.presentation.models

import com.nxtru.bondscalc.domain.models.BriefBondInfo

data class SearchScreenUIState(
    // pattern to search
    val pattern: String = "",
    val isSearching: Boolean = false,
    // found tickers
    val tickers: List<BriefBondInfo> = emptyList(),
)
