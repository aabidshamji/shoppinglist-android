package hu.aut.android.todorecyclerviewdemo.data

import android.arch.persistence.room.*

@Dao
interface TodoDAO {

    @Query("SELECT * FROM todo")
    fun findAllTodos(): List<Todo>

    @Insert
    fun insertTodo(item: Todo) : Long

    @Delete
    fun deleteTodo(item: Todo)

    @Update
    fun updateTodo(item: Todo)

    @Query("DELETE FROM [todo]")
    fun deleteAll()

}
