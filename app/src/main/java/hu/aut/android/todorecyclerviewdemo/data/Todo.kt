package hu.aut.android.todorecyclerviewdemo.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "todo")
data class Todo(
    @PrimaryKey(autoGenerate = true) var todoId: Long?,
    @ColumnInfo(name = "item") var createItem: String,
    @ColumnInfo(name = "type") var createType: String,
    @ColumnInfo(name = "price") var createPrice: String,
    @ColumnInfo(name = "description") var createDescription: String,
    @ColumnInfo(name = "purchased") var setPurchased: Boolean
) : Serializable
