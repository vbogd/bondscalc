package com.nxtru.bondscalc.presentation.models

import androidx.annotation.StringRes
import com.nxtru.bondscalc.domain.models.BriefBondInfo
import com.nxtru.bondscalc.R

data class SearchScreenUIState(
    // pattern to search
    val pattern: String = "",
    val isSearching: Boolean = false,
    // message to display if there is problem with finding tickers
    @StringRes
    val messageId: Int = R.string.empty,
    // found tickers
    val tickers: List<BriefBondInfo> = emptyList(),
)
