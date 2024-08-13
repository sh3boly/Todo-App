package com.example.todoapp.api

import TodosRoot
import com.example.todoapp.Constant.END_POINT
import retrofit2.Response
import retrofit2.http.GET

interface TodoApiCallable {
    @GET(END_POINT)
    suspend fun getTodos() : Response<TodosRoot>

}