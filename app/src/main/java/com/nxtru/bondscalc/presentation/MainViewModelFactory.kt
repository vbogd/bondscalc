package com.nxtru.bondscalc.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nxtru.bondscalc.data.repository.BondParamsRepositoryImpl
import com.nxtru.bondscalc.data.storage.SharedPrefsBondParamsStorage
import com.nxtru.bondscalc.domain.usecase.SaveBondParamsUseCase
import com.nxtru.bondscalc.domain.usecase.LoadBondParamsUseCase

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
        return MainViewModel(saveBondParamsUseCase, loadBondParamsUseCase) as T
    }
}