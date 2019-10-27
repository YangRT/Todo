package com.example.todo.data.network
import android.util.Log
import com.example.todo.data.network.api.*
import kotlin.coroutines.suspendCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
//网络请求
class ToDoNetwork{

    private val loginService = ServiceCreator.createLogin(LoginService::class.java)
    private val registerService:RegisterService = ServiceCreator.create(RegisterService::class.java)
    private val addEventService:AddEventService = ServiceCreator.create(AddEventService::class.java)
    private val deleteEventService:DeleteEventService = ServiceCreator.create(DeleteEventService::class.java)
    private val getEventService:GetEventService = ServiceCreator.create(GetEventService::class.java)
    private val updateEventService:UpdateEventService = ServiceCreator.create(UpdateEventService::class.java)
    private val updateStatusService:UpdateStatusService = ServiceCreator.create(UpdateStatusService::class.java)

    suspend fun login(username:String,password:String) = loginService.login(username,password).await()
    suspend fun register(username:String,password:String,repassword:String) = registerService.register(username,password,repassword).await()
    suspend fun getEvent(path:Int,status:Int?,type:Int?,priority:Int?,orderby:Int?) = getEventService.getEvent(path,status,type,priority,orderby).await()
    suspend fun deleteEvent(id:Int) = deleteEventService.deleteEvent(id).await()
    suspend fun addEvent(title:String,content:String,date:String?,type:Int) = addEventService.addEvent(title,content,date,type,null).await()
    suspend fun updateStatus(id:Int) = updateStatusService.updateStatus(id,1).await()
    suspend fun updateEvent(id:Int,title: String,content: String,date: String?,status: Int,type: Int) = updateEventService.updateEvent(id,title,content,date,status,type,null).await()
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }

    companion object {

        private var network: ToDoNetwork? = null

        fun getInstance(): ToDoNetwork {
            if (network == null) {
                synchronized(ToDoNetwork::class.java) {
                    if (network == null) {
                        network = ToDoNetwork()
                    }
                }
            }
            return network!!
        }

    }
}