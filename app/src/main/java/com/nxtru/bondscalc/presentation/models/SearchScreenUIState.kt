package com.nxtru.bondscalc.presentation.models

import com.nxtru.bondscalc.domain.models.BriefBondInfo

data class SearchScreenUIState(
    val pattern: String = "",
    val isSearching: Boolean = false,
    val tickers: List<BriefBondInfo> = emptyList(),
)
