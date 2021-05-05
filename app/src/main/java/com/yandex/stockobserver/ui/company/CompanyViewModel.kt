package com.yandex.stockobserver.ui.company


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.stockobserver.genralInfo.*
import com.yandex.stockobserver.repository.CompanyRepository
import com.yandex.stockobserver.utils.plusOneWeek
import com.yandex.stockobserver.utils.toString
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class CompanyViewModel @Inject constructor(private val companyRepository: CompanyRepository) :
    ViewModel() {
    private val _stockCandle = MutableLiveData<StockCandle>()
    val stockCandle: LiveData<StockCandle> = _stockCandle
    private val newStockCandle = mutableListOf<StockCandle>()

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite
    private var isFavoriteFirstValue: Boolean = false
    private val _isMainFragmentNeedUpdate = MutableLiveData<Boolean>()
    val isMainFragmentNeedUpdate: LiveData<Boolean> = _isMainFragmentNeedUpdate

    private val _news = MutableLiveData<List<CompanyNewsItem>>()
    val news: LiveData<List<CompanyNewsItem>> = _news
    private val newNews = mutableListOf<List<CompanyNewsItem>>()


    private lateinit var generalInfo: CompanyInfo

    fun setGeneralInfo(companyInfo: CompanyInfo) {
        generalInfo = companyInfo
        init()
    }

    private fun init() {
        initIsFavorite(generalInfo.isFavorite)
        getStockCandle(generalInfo.symbol)
        getNews(generalInfo.symbol, "", "")
    }

    private fun getStockCandle(symbol: String) {
        viewModelScope.launch {
            _stockCandle.value = companyRepository.getStockCandle(symbol)
        }
    }

    fun onFavoriteClick() {
        if (_isFavorite.value != null) {
            _isFavorite.value = !_isFavorite.value!!
            viewModelScope.launch {
                if (_isFavorite.value == true) {
                    Log.d("MainFragment", "onFavoriteClick: ")
                    generalInfo.isFavorite = _isFavorite.value!!
                    companyRepository.addFavorite(generalInfo)
                } else {
                    Log.d("MainFragment", "onFavoriteClick: -----")
                    companyRepository.deleteFavorite(generalInfo.symbol)
                }
            }
        }
    }

    private fun initIsFavorite(isFavorite: Boolean) {
        _isFavorite.value = isFavorite
        isFavoriteFirstValue = isFavorite
    }

    fun isMainNeedUpdate() {
        val isNeed = isFavoriteFirstValue != isFavorite.value
        _isMainFragmentNeedUpdate.value = isNeed
    }

    private fun getSummaryInfo() {
    }

    fun loadMoreNews(
        pastVisiblesItems: Int,
        visibleItemCount: Int,
        totalItemCount: Int
    ) {
        val loadIndent = 5
        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount - loadIndent) {

        }
    }

    private fun getNews(symbol: String, dateFrom: String, dateTo: String) {
        var dateTo = dateTo
        var dateFrom = dateFrom

        if (dateTo.isEmpty() && dateFrom.isEmpty()) {
            val format = "yyyy-MM-dd"
            val current = Date().toString(format)
            val inAWeek = Date().plusOneWeek(format)
            dateTo = current
            dateFrom = inAWeek
            Log.d("CompanyFragment", "getNews: $current $inAWeek")
        }

        viewModelScope.launch {
            _news.value = companyRepository.getNews(symbol, dateFrom, dateTo)
        }
    }

    private suspend fun getExecutive(): CompanyExecutive {
        val executive =
            viewModelScope.async { companyRepository.getExecutive(generalInfo.symbol) }
        return e(executive.await())
    }

    private fun e(executive: CompanyExecutive): CompanyExecutive {
        return executive
    }

}