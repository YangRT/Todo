package com.example.todo.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.todo.R
import com.example.todo.TodoApplication
import com.example.todo.ui.list.EventListActivity
import com.example.todo.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //根据 cookie 存储情况加载不同布局
        val sp = TodoApplication.context.getSharedPreferences("cookies_prefs", Context.MODE_PRIVATE)
        if(!TextUtils.isEmpty(sp.getString("https://www.wanandroid.com/user/login",""))){
            val intent =  Intent(this,EventListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }else{
            val intent =  Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        finish()
    }
}
