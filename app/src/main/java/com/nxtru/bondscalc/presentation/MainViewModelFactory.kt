package com.nxtru.bondscalc.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nxtru.bondscalc.data.bondinfo.moex.MoexRepository
import com.nxtru.bondscalc.data.repository.*
import com.nxtru.bondscalc.data.storage.SharedPrefsBondParamsStorage
import com.nxtru.bondscalc.domain.usecase.*
import com.nxtru.bondscalc.domain.usecase.bondinfo.*

class MainViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val bondParamsStorage by lazy(LazyThreadSafetyMode.NONE) {
        SharedPrefsBondParamsStorage(context)
    }
    private val userRepository by lazy(LazyThreadSafetyMode.NONE) {
        BondParamsRepositoryImpl(bondParamsStorage)
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
        val bondInfoRepository = BondInfoRepositoryImpl(bondParamsStorage)
        return MainViewModel(
            saveBondParamsUseCase = saveBondParamsUseCase,
            loadBondParamsUseCase = loadBondParamsUseCase,
            saveBondInfoUseCase = SaveBondInfoUseCase(bondInfoRepository),
            loadBondInfoUseCase = LoadBondInfoUseCase(bondInfoRepository),
            searchTickersUseCase = SearchTickersUseCase(bondInfoService),
            loadBondInfoDataUseCase = LoadBondInfoDataUseCase(bondInfoService)
        ) as T
    }
}
