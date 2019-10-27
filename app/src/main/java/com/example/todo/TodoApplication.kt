package com.example.todo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.ContextWrapper

import java.security.AccessControlContext

class TodoApplication:Application(){


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
    companion object {
       lateinit var context: Context
    }
}