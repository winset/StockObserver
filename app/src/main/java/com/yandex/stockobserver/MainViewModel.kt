package com.yandex.stockobserver

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.stockobserver.genralInfo.CompanyInfo
import com.yandex.stockobserver.genralInfo.HoldingsItem
import kotlinx.coroutines.launch

class MainViewModel(private val repositoryImpl: HoldingRepositoryImpl = HoldingRepositoryImpl()) :
    ViewModel() {
    private val _vooCompanies = MutableLiveData<List<CompanyInfo>>()
    val vooCompanies: LiveData<List<CompanyInfo>> = _vooCompanies

    private val _favouriteCompanies = MutableLiveData<List<CompanyInfo>>()
    val favouriteCompanies: LiveData<List<CompanyInfo>> = _favouriteCompanies


    private val _cusip = MutableLiveData<String>()
     val cusip:LiveData<String> = _cusip


    init {
        getHoldings()
    }

    private fun getHoldings() {
        viewModelScope.launch {
            val r = (repositoryImpl.getHolding(1).holdings as List<HoldingsItem>?)!!
            val c = mutableListOf<CompanyInfo>()
            r.forEachIndexed { index, holdingsItem ->
                c.add(CompanyInfo(index,holdingsItem.symbol,holdingsItem.cusip,holdingsItem.name))
            }
            _vooCompanies.value = c
        }
    }

    fun onItemClick(cusip:String){
        _cusip.value = cusip
        Log.d("TAG", "onItemClick: "+ cusip)
    }

    fun onFavoriteClick(id:Int){

    }


}