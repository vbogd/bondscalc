package com.nxtru.bondscalc.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nxtru.bondscalc.data.bondinfo.moex.MoexRepository
import com.nxtru.bondscalc.data.repository.BondParamsRepositoryImpl
import com.nxtru.bondscalc.data.storage.SharedPrefsBondParamsStorage
import com.nxtru.bondscalc.domain.usecase.SaveBondParamsUseCase
import com.nxtru.bondscalc.domain.usecase.LoadBondParamsUseCase
import com.nxtru.bondscalc.domain.usecase.bondinfo.LoadBondInfoUseCase
import com.nxtru.bondscalc.domain.usecase.bondinfo.SearchTickersUseCase

class MainViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val userRepository by lazy(LazyThreadSafetyMode.NONE) {
        BondParamsRepositoryImpl(SharedPrefsBondParamsStorage(context))
    }
    private val saveBondParamsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        SaveBondParamsUseCase(userRepository)
    }
    private val loadBondParamsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        LoadBondParamsUseCase(userRepository)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val bondInfoService = MoexRepository()
        return MainViewModel(
            saveBondParamsUseCase,
            loadBondParamsUseCase,
            SearchTickersUseCase(bondInfoService),
            LoadBondInfoUseCase(bondInfoService)
        ) as T
    }
}
