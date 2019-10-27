package com.example.todo.data.network.api

import com.example.todo.data.model.RequestInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterService{
    @FormUrlEncoded
    @POST("user/register")
    fun register(@Field("username")username:String,@Field("password")password:String,@Field("repassword")repassword:String):Call<RequestInfo>
}