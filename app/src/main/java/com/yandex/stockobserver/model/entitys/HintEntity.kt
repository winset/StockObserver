package com.yandex.stockobserver.model.entitys

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yandex.stockobserver.db.TableNames
import com.yandex.stockobserver.model.Hint

@Entity(tableName = TableNames.LOOKING_FOR_HINT)
class HintEntity(
    @PrimaryKey
    @ColumnInfo(name = "hint")
    val hint: String
) {
    constructor(hint: Hint) : this(
        hint = hint.hint
    )


    fun convert(): Hint {
        return Hint(
            hint = hint
        )
    }

}