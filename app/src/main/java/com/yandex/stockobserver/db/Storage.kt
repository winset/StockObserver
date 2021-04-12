package com.yandex.stockobserver.db

import androidx.room.withTransaction
import com.yandex.stockobserver.genralInfo.Hint
import com.yandex.stockobserver.genralInfo.entitys.CompanyInfoEntity
import com.yandex.stockobserver.genralInfo.entitys.HintEntity

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

    suspend fun deleteFromFavorite(cusip: String) {
        database.withTransaction {
            favoriteDao.deleteFavorite(cusip)
        }
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

}