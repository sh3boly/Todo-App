package com.example.todoapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM Todo")
    fun getAll(): Flow<List<Todo>>

    @Upsert
    fun upsertTodo(vararg todo: Todo)

    @Delete
    fun delete(todo: Todo)

}