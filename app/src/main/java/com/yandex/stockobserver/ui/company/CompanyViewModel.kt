package com.yandex.stockobserver.ui.company

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.stockobserver.genralInfo.StockCandle
import com.yandex.stockobserver.repository.CompanyRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CompanyViewModel @Inject constructor(private val companyRepository: CompanyRepository):ViewModel() {
    private val _stockCandle = MutableLiveData<StockCandle>()
    val stockCandle:LiveData<StockCandle> = _stockCandle
    private val newStockCandle = mutableListOf<StockCandle>()


    init {
        getStockCandle()
    }

    private fun getStockCandle(){
        viewModelScope.launch {
            _stockCandle.value = companyRepository.getStockCandle()
        }
    }

}