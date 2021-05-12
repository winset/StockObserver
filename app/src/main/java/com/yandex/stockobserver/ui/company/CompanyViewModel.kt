package com.yandex.stockobserver.ui.company


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.stockobserver.api.QuoteWebsocket
import com.yandex.stockobserver.model.*
import com.yandex.stockobserver.repository.CompanyRepository
import com.yandex.stockobserver.utils.fromStringToDate
import com.yandex.stockobserver.utils.minusOneWeek
import com.yandex.stockobserver.utils.toString
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
    private val newNews = mutableListOf<CompanyNewsItem>()
    private val _haveNews = MutableLiveData<Boolean>()
    val haveNews: LiveData<Boolean> = _haveNews
    private var newsDateTo: String = ""
    private var newsDateFrom: String = ""

    private val _summary = MutableLiveData<CompanyGeneral>()
    val summary: LiveData<CompanyGeneral> = _summary

    private val _currentPrice = MutableLiveData<Double>()
    val currentPrice: LiveData<Double> = _currentPrice

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private lateinit var generalInfo: CompanyInfo

    fun setGeneralInfo(companyInfo: CompanyInfo) {
        generalInfo = companyInfo
        init()
    }

    private fun init() {
        initIsFavorite(generalInfo.isFavorite)
        getStockCandle(generalInfo.symbol)
        getNews(generalInfo.symbol, newsDateFrom, newsDateTo)
        getSummaryInfo(generalInfo.symbol)

        getCurrentQuote()
    }

    private fun getStockCandle(symbol: String) {
        viewModelScope.launch {
            _stockCandle.value = companyRepository.getStockCandle(symbol)
        }
    }

    private fun getCurrentQuote() {
        viewModelScope.launch {
            companyRepository.initQuoteSocket(generalInfo.symbol)
        }
        val currentPrice = (Math.round(generalInfo.price*100).toDouble()/100)
        _currentPrice.value = currentPrice
        companyRepository.getCurrentPrice(generalInfo.symbol).addMessageHandler(object :
            QuoteWebsocket.MessageHandler {
            override fun handleMessage(quoteTicker: QuoteTicker) {
                var res = quoteTicker.data.first().currentPrice
                if (res >=0) {
                    res = Math.round(res*100).toDouble()/100
                    _currentPrice.postValue(res)
                }
            }
        })
    }

    private fun getWe(i: Double) {
        _currentPrice.value = i
    }

    fun closeQuoteWebsocket() {
        viewModelScope.launch {
            companyRepository.closeQuoteWebSocket()
        }
    }
    
    fun onFavoriteClick() {
        if (_isFavorite.value != null) {
            _isFavorite.value = !_isFavorite.value!!
            viewModelScope.launch {
                if (_isFavorite.value == true) {
                    generalInfo.isFavorite = _isFavorite.value!!
                    companyRepository.addFavorite(generalInfo)
                } else {
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

    private fun getSummaryInfo(symbol: String) {
        viewModelScope.launch {
            try {
                _summary.value = companyRepository.getGeneralInfoBySymbol(symbol)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    private fun getNews(symbol: String, dateFrom: String, dateTo: String) {
        var dateTo = dateTo
        var dateFrom = dateFrom
        val format = "yyyy-MM-dd"
        if (dateTo.isEmpty() && dateFrom.isEmpty()) {
            val current = Date().toString(format)
            val inAWeek = Date().minusOneWeek()
            dateTo = current
            dateFrom = inAWeek.toString(format)
        }
        viewModelScope.launch {
            try {
                var news = companyRepository.getNews(symbol, dateFrom, dateTo)
                if (news.isNotEmpty()) {
                    newNews.addAll(news)
                    _news.value = newNews
                } else {
                    val newDateFrom =
                        fromStringToDate(dateFrom, format).minusOneWeek().toString(format)
                    news = companyRepository.getNews(symbol, newDateFrom, dateTo)
                }
                if (news.isEmpty())
                    _haveNews.value = false
                else
                    _haveNews.value = true
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}