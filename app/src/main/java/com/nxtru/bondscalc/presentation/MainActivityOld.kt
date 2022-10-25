package com.nxtru.bondscalc.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.nxtru.bondscalc.R

class MainActivityOld : AppCompatActivity() {

    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vm = ViewModelProvider(this, MainViewModelFactory(applicationContext))[MainViewModel::class.java]

        findViewById<TextView>(R.id.result).let { resultRub ->
            vm.resultRubLive.observe(this) { resultRub.text = it }
        }

        findViewById<TextView>(R.id.result_percent).let { resultPercent ->
            vm.resultYTMLive.observe(this) { resultPercent.text = it }
        }

        bindTextView(findViewById(R.id.ticker), vm.ticker, vm::setTicker)
        bindTextView(findViewById(R.id.commission), vm.commission, vm::setCommission)
        bindTextView(findViewById(R.id.tax), vm.tax, vm::setTax)
        bindTextView(findViewById(R.id.coupon), vm.coupon, vm::setCoupon)
        bindTextView(findViewById(R.id.par_value), vm.parValue, vm::setParValue)
        bindTextView(findViewById(R.id.buy_price), vm.buyPrice, vm::setBuyPrice)
        bindTextView(findViewById(R.id.buy_date), vm.buyDate, vm::setBuyDate)
        bindTextView(findViewById(R.id.sell_price), vm.sellPrice, vm::setSellPrice)
        bindTextView(findViewById(R.id.sell_date), vm.sellDate, vm::setSellDate)
        bindCheckbox(findViewById(R.id.till_maturity), vm.tillMaturity, vm::setTillMaturity)
    }

    private fun bindTextView(
        view: TextView,
        liveData: LiveData<String>,
        vmSetter: (String) -> Unit
    ) {
        view.doAfterTextChanged { vmSetter(it.toString()) }
        liveData.observe(this) {
            if (it != view.text.toString()) view.text = it
        }
    }

    private fun bindCheckbox(
        view: CheckBox,
        liveData: LiveData<Boolean>,
        vmSetter: (Boolean) -> Unit
    ) {
        view.setOnClickListener { vmSetter(view.isChecked) }
        liveData.observe(this) {
            if (it != view.isChecked) view.isChecked = it
        }
    }
}
