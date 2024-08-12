package com.example.todoapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TodoDao {
    @Query("SELECT * FROM Todo")
    fun getAll(): List<Todo>

    @Upsert
    fun upsertTodo(vararg todo: Todo)

    @Delete
    fun delete(todo: Todo)

}