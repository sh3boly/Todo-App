package com.example.todoapp.api

import com.example.todoapp.Constant.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val callable: TodoApiCallable by lazy { retrofit.create(TodoApiCallable::class.java) }

}