package com.example.todo.data

import android.util.Log
import com.example.todo.data.network.ToDoNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository private constructor(private val toDoNetwork: ToDoNetwork) {

    suspend fun getEvent(path:Int,status:Int?,type:Int?,priority:Int?,orderby:Int?)= withContext(Dispatchers.IO){
        val eventInfo = toDoNetwork.getEvent(path,status,type,priority,orderby)
        eventInfo
    }

    suspend fun addEvent(title:String,content:String,date:String?,type:Int) = withContext(Dispatchers.IO){
        val addInfo = toDoNetwork.addEvent(title, content, date, type)
        addInfo
    }

    suspend fun deleteEvent(id:Int) = withContext(Dispatchers.IO){
        val deleteInfo = toDoNetwork.deleteEvent(id)
        deleteInfo
    }

    suspend fun updateEvent(id:Int,title: String,content: String,date: String?,status: Int,type: Int) = withContext(Dispatchers.IO){
        val updateInfo = toDoNetwork.updateEvent(id, title, content, date, status, type)
        updateInfo
    }

    suspend fun updateStatus(id:Int)= withContext(Dispatchers.IO){
        val updateStatus = toDoNetwork.updateStatus(id)
        Log.e("unfinished","updateInfo")
        updateStatus
    }

    companion object {
        private lateinit var instance: EventRepository

        fun getInstance(toDoNetwork: ToDoNetwork): EventRepository {
            if(!Companion::instance.isInitialized){
                synchronized(LoginRegisterRepository::class.java){
                    instance =
                        EventRepository(toDoNetwork)
                }
            }
            return instance
        }
    }
}