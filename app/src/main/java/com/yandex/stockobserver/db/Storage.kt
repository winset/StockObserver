package com.yandex.stockobserver.db

import androidx.room.withTransaction
import com.yandex.stockobserver.model.Hint
import com.yandex.stockobserver.model.entitys.CompanyInfoEntity
import com.yandex.stockobserver.model.entitys.HintEntity

class Storage(private val database: StockDatabase) {

    private val favoriteDao = database.getFavoriteStock()
    private val hintDao= database.getHint()

    suspend fun insertFavorite(companyInfoEntity: CompanyInfoEntity) {
        database.withTransaction {
            favoriteDao.insertFavorite(companyInfoEntity)
        }
    }

    fun getAllFavorite(): List<CompanyInfoEntity> {
        return favoriteDao.getAllFavorite()
    }

    suspend fun deleteFromFavorite(symbol: String) {
        database.withTransaction {
            favoriteDao.deleteFavorite(symbol)
        }
    }

    suspend fun isFavorite(symbol:String):Boolean{
        return favoriteDao.isFavorite(symbol)
    }

    suspend fun addHint(hint: Hint) {
        database.withTransaction {
            hintDao.insertHint(HintEntity(hint))
        }
    }

    suspend fun deleteHint(hint: Hint){
        database.withTransaction {
            hintDao.deleteFavorite(hint = hint.hint)
        }
    }

      suspend fun getAllHints():List<Hint>{
        return hintDao.getAllHint().map { it.convert() }
    }

    suspend fun isHintInDb(symbol: String):Boolean{
        return hintDao.isHintInDb(symbol)
    }



}