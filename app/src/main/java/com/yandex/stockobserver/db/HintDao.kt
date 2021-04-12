package com.yandex.stockobserver.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yandex.stockobserver.genralInfo.entitys.CompanyInfoEntity
import com.yandex.stockobserver.genralInfo.entitys.HintEntity

@Dao
interface HintDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHint(hintEntity: HintEntity)

    @Query("SELECT * FROM hintdb")
    fun getAllHint():List<HintEntity>

    @Query("DELETE FROM hintdb WHERE hint =:hint")
    fun deleteFavorite(hint:String)

}