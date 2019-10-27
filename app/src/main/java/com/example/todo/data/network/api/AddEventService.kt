package com.example.todo.data.network.api

import com.example.todo.data.model.RequestInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AddEventService{
    @FormUrlEncoded
    @POST("lg/todo/add/json")
    fun addEvent(@Field("title")title:String,@Field("content")content:String,@Field("date")date:String?,@Field("type")type:Int?,@Field("priority")priority:Int?):Call<RequestInfo>
}