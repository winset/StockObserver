package com.yandex.stockobserver.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.stockobserver.genralInfo.*
import com.yandex.stockobserver.repository.HoldingRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(private val holdingRepository: HoldingRepository) :
    ViewModel() {

    private val _vooCompanies = MutableLiveData<List<CompanyInfo>>()
    val vooCompanies: LiveData<List<CompanyInfo>> = _vooCompanies
    private val newVooComp = mutableListOf<CompanyInfo>()

    private val _favouriteCompanies = MutableLiveData<List<CompanyInfo>>()
    val favouriteCompanies: LiveData<List<CompanyInfo>> = _favouriteCompanies

    private val _searchCompanies = MutableLiveData<List<CompanyInfo>>()
    val searchCompanies: LiveData<List<CompanyInfo>> = _searchCompanies
    private val newSearchCompanies = mutableListOf<CompanyInfo>()

    private val _popularHint = MutableLiveData<List<Hint>>()
    val popularHint: LiveData<List<Hint>> = _popularHint

    private val _lookingHint = MutableLiveData<List<Hint>>()
    val lookingHint: LiveData<List<Hint>> = _lookingHint
    private val newHint = mutableListOf<Hint>()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private lateinit var holdingsList: ETFHoldings
    private lateinit var searchList: ETFHoldings

    private val _symbol = MutableLiveData<String>()
    val symbol: LiveData<String> = _symbol

    private var vooPage: Int = 0
    private var searchPage: Int = 0
    private var loadMoreStocks = true
    private var loadMoreSimilar = true

    init {
        viewModelScope.launch {
            getHoldingsList()
            if (::holdingsList.isInitialized) {
                getHoldings(holdingsList, 0, MainFragment.TOP_STOCKS)
                getFavorites()
                getHint()
            }
        }
    }

    private suspend fun getHoldingsList() {
        try {
            holdingsList = holdingRepository.getHolding(0)
        } catch (e: Exception) {
            Log.d("TAG", "getHoldingsList: " + e.message)
            getHoldingsList()
        }
    }

    private fun getHoldings(holdingsList: ETFHoldings, page: Int, contentType: String) {
        viewModelScope.launch {
            holdingRepository.getVOOCompanies(holdingsList, page).catch {
                Log.d("TAG", "getHoldings111: " + it.message)
                if (page != 0) {
                    when (contentType) {
                        MainFragment.TOP_STOCKS -> vooPage--
                        MainFragment.SIMILAR_SEARCH -> {
                            Log.d("SEARCH", "getHoldings111: " + searchPage--)
                            searchPage--
                        }
                    }
                }
                _error.value = it.message
            }.collect {

                if (contentType == MainFragment.TOP_STOCKS) {
                    if (!it.isNullOrEmpty()) {
                        newVooComp.addAll(it)
                        Log.d("TAG", "getHoldings: " + newVooComp.size)
                        _vooCompanies.value = newVooComp
                        loadMoreStocks = true
                    }
                }
                if (contentType == MainFragment.SIMILAR_SEARCH) {
                    newSearchCompanies.addAll(it)
                    _searchCompanies.value = newSearchCompanies
                    loadMoreSimilar = true
                }
            }
        }
    }

    private fun getFavorites() {
        viewModelScope.launch {
            holdingRepository.getFavorites().collect {
                _favouriteCompanies.value = it
            }
        }
    }

    private fun getHint() {
        viewModelScope.launch {
            _popularHint.value = holdingRepository.getPopularHint(holdingsList)
            newHint.addAll(holdingRepository.getLookingHint())
            _lookingHint.value = newHint
        }
    }

    fun addLookingForHint(symbol: String) {
        viewModelScope.launch {
            newHint.add(Hint(symbol))
            _lookingHint.value = newHint
            holdingRepository.addNewHint(symbol)
        }
    }

    fun onItemClick(symbol: String) {
        _symbol.value = symbol
    }

    fun search(symbol: String) {
        var symbol = symbol.filter { !it.isWhitespace() }
        if (symbol.length > 10) {
            symbol = symbol.substring(0, 9)
        }
        symbol.replace(".", "")
        //This code is needed,
        //otherwise api has problems and we get an empty answer
        //may be it's ok? idk
        Log.d("SEARCH", "search: $symbol")
        searchPage = 0
        loadMoreSimilar = true
        viewModelScope.launch {
            newSearchCompanies.clear()
            val searchSimilar = holdingRepository.getSimilar(symbol)
            val holdingsItems = mutableListOf<HoldingsItem>()
            searchSimilar.result.forEach {
                holdingsItems.add(
                    HoldingsItem(it.displaySymbol, "", "", 0f, 0.0, 0f, "")
                )
            }
            val holdingsList = ETFHoldings("", "", holdingsItems, searchSimilar.count)
            getHoldings(holdingsList, 0, MainFragment.SIMILAR_SEARCH)
            searchList = holdingsList
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
                holdingRepository.deleteFavorite(companyInfo.symbol)
            } else {
                companyInfo.isFavorite = true
                holdingRepository.addFavorite(companyInfo)
            }

            if (contentType == MainFragment.TOP_STOCKS) {
                newVooComp[index] = companyInfo
                _vooCompanies.value = newVooComp
            } else {
                newVooComp.forEachIndexed { index, stockCompany ->
                    if (stockCompany.symbol == companyInfo.symbol) {
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
        totalItemCount: Int,
        contentType: String
    ) {
        val loadIndent = 3
        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount - loadIndent) {
            if (contentType == MainFragment.TOP_STOCKS) {
                if (loadMoreStocks && (vooPage + 1) * 15 <= holdingsList.holdings.size) {
                    vooPage++
                    getHoldings(holdingsList, vooPage, contentType)
                    loadMoreStocks = false
                }
            }
            if (contentType == MainFragment.SIMILAR_SEARCH) {
                if (loadMoreSimilar && (searchPage + 1) * 15 <= searchList.holdings.size) {
                    searchPage++
                    getHoldings(searchList, searchPage, contentType)
                    loadMoreSimilar = false
                }
            }
        }
    }
}