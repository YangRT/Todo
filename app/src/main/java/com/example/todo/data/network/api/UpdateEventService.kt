package com.example.todo.data.network.api

import com.example.todo.data.model.RequestInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface UpdateEventService{
    @FormUrlEncoded
    @POST("lg/todo/update/{id}/json")
    fun updateEvent(@Path("id")id:Int, @Field("title")title:String, @Field("content")content:String, @Field("date")date:String?, @Field("status")status:Int, @Field("type")type:Int?, @Field("priority")priority:Int?):Call<RequestInfo>
}
