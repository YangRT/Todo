package com.example.todo.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.example.todo.R
import com.example.todo.TodoApplication.Companion.context

class PopupWindowFactory {

    companion object {

        fun createWindow(context: Context?,type:Int):PopupWindow{
            lateinit var view:View
            when (type) {
                Type.UNFINISHED_EVENT_WINDOW -> view = LayoutInflater.from(context).inflate(R.layout.popup_window2,null,false)
                Type.ACCOUNT_WINDOW -> view = LayoutInflater.from(context).inflate(R.layout.popup_window,null,false)
                Type.FINISHED_EVENT_WINDOW -> view = LayoutInflater.from(context).inflate(R.layout.popup_window3,null,false)
            }
            val window = PopupWindow(view,500, WindowManager.LayoutParams.WRAP_CONTENT)
            window.setOnDismissListener(PopupWindow.OnDismissListener {
                val lp = (context as Activity).window.attributes
                lp.alpha = 1.0f; //设置透明度
                (context as Activity).window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                (context as Activity).window.attributes = lp;
            })
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.isOutsideTouchable = true
            window.isTouchable = true
            window.isFocusable = true
            return window
        }

    }


}