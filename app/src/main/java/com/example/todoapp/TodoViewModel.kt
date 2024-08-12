package com.example.todoapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.database.Todo
import com.example.todoapp.database.TodoDatabase
import com.example.todoapp.model.TodoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(app: Application) : AndroidViewModel(app) {
    private val db: TodoDatabase = TodoDatabase.getInstance(app)
    fun upsertTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) { db.todoDao().upsertTodo(todo) }
    }

    fun deleteTodo(todo: Todo){
        viewModelScope.launch(Dispatchers.IO) { db.todoDao().delete(todo) }
    }

    fun getTodos() = db.todoDao().getAll()
}