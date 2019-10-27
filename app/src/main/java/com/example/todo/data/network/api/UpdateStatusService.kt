package com.example.todo.data.network.api

import com.example.todo.data.model.RequestInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface UpdateStatusService{
    @FormUrlEncoded
    @POST("lg/todo/done/{id}/json")
    fun updateStatus(@Path("id")id:Int, @Field("status")status:Int):Call<RequestInfo>
}