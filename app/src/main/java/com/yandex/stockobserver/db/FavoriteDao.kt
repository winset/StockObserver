package com.yandex.stockobserver.db

import androidx.room.*
import com.yandex.stockobserver.genralInfo.entitys.CompanyInfoEntity

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(companyInfoEntity: CompanyInfoEntity)

    @Query("DELETE FROM stockgeneralinfodb WHERE cusip =:cusip")
    fun deleteFavorite(cusip:String)

    @Query("SELECT * FROM stockgeneralinfodb")
    fun getAllFavorite():List<CompanyInfoEntity>
}