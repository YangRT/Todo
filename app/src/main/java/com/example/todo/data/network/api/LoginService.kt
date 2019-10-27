package com.example.todo.data.network.api

import com.example.todo.data.model.RequestInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService{
    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username")username:String,@Field("password")password:String):Call<RequestInfo>
}