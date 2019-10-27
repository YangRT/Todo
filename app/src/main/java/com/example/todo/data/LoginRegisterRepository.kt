package com.example.todo.data

import com.example.todo.data.network.ToDoNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRegisterRepository private constructor(private val toDoNetwork: ToDoNetwork){

    suspend fun login(username:String,password:String)= withContext(Dispatchers.IO){
        val info = toDoNetwork.login(username, password)
        info
    }

    suspend fun register(username:String,password:String,repassword:String) = withContext(Dispatchers.IO){
        val info = toDoNetwork.register(username, password, repassword)
        info
    }

    companion object {
        private lateinit var instance: LoginRegisterRepository

        fun getInstance(toDoNetwork: ToDoNetwork):LoginRegisterRepository{
            if(!::instance.isInitialized){
                synchronized(LoginRegisterRepository::class.java){
                    instance = LoginRegisterRepository(toDoNetwork)
                }
            }
            return instance
        }
    }
}