package com.example.todo.data.network.api

import com.example.todo.data.model.EventInfo
import com.example.todo.data.model.RequestInfo
import retrofit2.Call
import retrofit2.http.*

interface GetEventService{
    @GET("lg/todo/v2/list/{path}/json")
    fun getEvent(@Path("path")path:Int, @Query("status")status:Int?, @Query("type")type:Int?, @Query("priority")priority:Int?, @Query("orderby")orderby:Int?):Call<EventInfo>
}