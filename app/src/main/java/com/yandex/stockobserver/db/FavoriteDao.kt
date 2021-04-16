package com.yandex.stockobserver.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yandex.stockobserver.genralInfo.entitys.CompanyInfoEntity

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(companyInfoEntity: CompanyInfoEntity)

    @Query("DELETE FROM stockgeneralinfodb WHERE symbol =:symbol")
    fun deleteFavorite(symbol: String)

    @Query("SELECT * FROM stockgeneralinfodb")
    fun getAllFavorite(): List<CompanyInfoEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM stockgeneralinfodb WHERE symbol=:symbol)")
    suspend fun isFavorite(symbol: String): Boolean
}