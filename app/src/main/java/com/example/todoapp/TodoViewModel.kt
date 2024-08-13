package com.example.todoapp

import TodoModel
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.api.RetrofitFactory
import com.example.todoapp.database.Todo
import com.example.todoapp.database.TodoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.log

class TodoViewModel(app: Application) : AndroidViewModel(app) {
    private val _todos = MutableStateFlow<List<TodoModel>>(emptyList())
    val todos = _todos

    init {
        getTodoApi()
    }

    private val db: TodoDatabase = TodoDatabase.getInstance(app)
    fun upsertTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) { db.todoDao().upsertTodo(todo) }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) { db.todoDao().delete(todo) }
    }

    fun getTodos(): Flow<List<Todo>> = db.todoDao().getAll()

    fun getTodo(id: Int): Flow<Todo> = db.todoDao().getTodo(id)

    fun getTodoApi() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitFactory.callable.getTodos()
                if (response.isSuccessful) {
                    _todos.update { response.body()!! }
                }
            }
            catch (e: Exception) {
                Log.d("trace", e.message.toString())
            }
        }
    }

    fun listTodoMapper(todoList: List<TodoModel>): List<Todo> {
        val todos = todoList.map { apiTodoMapper(it) }
        todos.forEach { upsertTodo(it) }
        return todos
    }
}

fun apiTodoMapper(todo: TodoModel): Todo {
    return Todo(
        title = "Api Todo",
        description = todo.title,
        status = todo.completed
    )
}