package com.yandex.stockobserver

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.stockobserver.genralInfo.CompanyInfo
import com.yandex.stockobserver.ui.MainFragment
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val repositoryImpl: HoldingRepositoryImpl = HoldingRepositoryImpl()) :
    ViewModel() {
    private val _vooCompanies = MutableLiveData<List<CompanyInfo>>()
    val vooCompanies: LiveData<List<CompanyInfo>> = _vooCompanies
    private val newVooComp = mutableListOf<CompanyInfo>()

    private val _favouriteCompanies = MutableLiveData<List<CompanyInfo>>()
    val favouriteCompanies: LiveData<List<CompanyInfo>> = _favouriteCompanies


    private val _cusip = MutableLiveData<String>()
    val cusip: LiveData<String> = _cusip

    private var vooPage:Int = 0

    init {
        getHoldings(vooPage)
        getFavorites()
    }

    private fun getHoldings(page: Int) {
        viewModelScope.launch {
            repositoryImpl.getVOOCompanies(page).catch {
                Log.d("TAG", "getHoldings: " +vooPage)
            }.collect {
                if (!it.isNullOrEmpty()){
                    newVooComp.addAll(it)
                    Log.d("TAG", "getHoldings: "+ newVooComp.size)
                    _vooCompanies.value = newVooComp
                }
            }
        }
    }

    private fun getFavorites(){
        viewModelScope.launch {
            repositoryImpl.getFavorites().collect {
                _favouriteCompanies.value = it
            }
        }
    }

    fun onItemClick(cusip: String) {
        _cusip.value = cusip

    }

     fun onFavoriteClick(companyInfo: CompanyInfo,index:Int,isFavorite:Boolean,contentType:String) {
         viewModelScope.launch {
             if (!isFavorite){
                 companyInfo.isFavorite = false
                 repositoryImpl.deleteFavorite(companyInfo.cusip)
             }else{
                 companyInfo.isFavorite = true
                 repositoryImpl.addFavorite(companyInfo)
             }

             if (contentType==MainFragment.TOP_STOCKS){
                 newVooComp[index] = companyInfo
                 _vooCompanies.value = newVooComp
             }else{
                 newVooComp.forEachIndexed { index,stockCompany ->
                     if (stockCompany.cusip == companyInfo.cusip){
                      newVooComp[index] = companyInfo
                      _vooCompanies.value = newVooComp
                     }
                 }
             }

             getFavorites()
         }
    }

    fun loadOnScroll(
        pastVisiblesItems: Int,
        visibleItemCount: Int,
        totalItemCount: Int
    ) {
        val loadIndent = 5
        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount - loadIndent) {
            vooPage++
            getHoldings(1)
        }
    }


}