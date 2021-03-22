package com.yandex.stockobserver

import android.util.Log
import android.util.TimeUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.stockobserver.genralInfo.CompanyInfo
import com.yandex.stockobserver.genralInfo.ETFHoldings
import com.yandex.stockobserver.genralInfo.Hint
import com.yandex.stockobserver.ui.MainFragment
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.*

class MainViewModel(private val repositoryImpl: HoldingRepositoryImpl = HoldingRepositoryImpl()) :
    ViewModel() {
    private val _vooCompanies = MutableLiveData<List<CompanyInfo>>()
    val vooCompanies: LiveData<List<CompanyInfo>> = _vooCompanies
    private val newVooComp = mutableListOf<CompanyInfo>()

    private val _favouriteCompanies = MutableLiveData<List<CompanyInfo>>()
    val favouriteCompanies: LiveData<List<CompanyInfo>> = _favouriteCompanies

    private val _popularHint = MutableLiveData<List<Hint>>()
    val popularHint: LiveData<List<Hint>> = _popularHint

    private val _lookingHint = MutableLiveData<List<Hint>>()
    val lookingHint: LiveData<List<Hint>> = _lookingHint

    private lateinit var holdingsList: ETFHoldings

    private val _cusip = MutableLiveData<String>()
    val cusip: LiveData<String> = _cusip

    private var vooPage: Int = 0

    init {
        viewModelScope.launch {

            getHoldingsList()
            if (::holdingsList.isInitialized) {
                getHoldings(holdingsList, 0)
              //  getHoldings(holdingsList, 1)
                getFavorites()
                getHint()
            }
        }

    }

    private suspend fun getHoldingsList() {
        try {
            holdingsList = repositoryImpl.getHolding(0)
        } catch (e: Exception) {
            Log.d("TAG", "getHoldingsList: " + e.message)
            getHoldingsList()
        }
    }

    private fun getHoldings(holdingsList: ETFHoldings, page: Int) {
        viewModelScope.launch {
            repositoryImpl.getVOOCompanies(holdingsList, page).catch {
                Log.d("TAG", "getHoldings111: " + it.message)
            }.collect {
                if (!it.isNullOrEmpty()) {
                    newVooComp.addAll(it)
                    Log.d("TAG", "getHoldings: " + newVooComp.size)
                    _vooCompanies.value = newVooComp
                }
            }
        }
    }

    private fun getFavorites() {
        viewModelScope.launch {
            repositoryImpl.getFavorites().collect {
                _favouriteCompanies.value = it
            }
        }
    }

     private fun getHint() {
         viewModelScope.launch {
             _popularHint.value = repositoryImpl.getPopularHint(holdingsList)
         }
    }

    fun addLookingForHint(symbol: String){

    }

    fun onItemClick(cusip: String) {
        _cusip.value = cusip
    }

    fun search(symbol: String) {
        Log.d("SEARCH", "search: " + symbol)
        viewModelScope.launch {
            repositoryImpl.getSimilar(symbol)
        }
    }

    fun onFavoriteClick(
        companyInfo: CompanyInfo,
        index: Int,
        isFavorite: Boolean,
        contentType: String
    ) {
        viewModelScope.launch {
            if (!isFavorite) {
                companyInfo.isFavorite = false
                repositoryImpl.deleteFavorite(companyInfo.cusip)
            } else {
                companyInfo.isFavorite = true
                repositoryImpl.addFavorite(companyInfo)
            }

            if (contentType == MainFragment.TOP_STOCKS) {
                newVooComp[index] = companyInfo
                _vooCompanies.value = newVooComp
            } else {
                newVooComp.forEachIndexed { index, stockCompany ->
                    if (stockCompany.cusip == companyInfo.cusip) {
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
            // getHoldings(holdingsList,2)
        }
    }


}