package com.yandex.stockobserver.db

import androidx.room.withTransaction
import com.yandex.stockobserver.genralInfo.entitys.CompanyInfoEntity

class Storage(private val database: StockDatabase) {

    private val favoriteDao = database.getFavoriteStock()

    suspend fun insertFavorite(companyInfoEntity: CompanyInfoEntity){
        database.withTransaction {
            favoriteDao.insertFavorite(companyInfoEntity)
        }
    }

   suspend fun getAllFavorite():List<CompanyInfoEntity>{
        return favoriteDao.getAllFavorite()
    }

    suspend fun deleteFromFavorite(cusip:String){
        database.withTransaction {
            favoriteDao.deleteFavorite(cusip)
        }
    }


}