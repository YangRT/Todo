package com.example.todo.data.network.api

import com.example.todo.data.model.RequestInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface DeleteEventService{
    @POST("lg/todo/delete/{id}/json")
    fun deleteEvent(@Path("id")id:Int):Call<RequestInfo>
}