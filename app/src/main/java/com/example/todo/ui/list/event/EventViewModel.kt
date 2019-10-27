package com.example.todo.ui.list.event

import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.TodoApplication
import com.example.todo.data.EventRepository
import com.example.todo.data.model.RequestInfo
import kotlinx.coroutines.launch

class EventViewModel(private val repository: EventRepository):ViewModel() {
    var detail = ObservableBoolean()
    var addInfo = MutableLiveData<RequestInfo>()
    var updateInfo = MutableLiveData<RequestInfo>()

    var life = ObservableBoolean()
    var work = ObservableBoolean()
    var learn = ObservableBoolean()
    var other = ObservableBoolean()

    fun addEvent(title:String,content:String,date:String?,type:Int){
        launch({
            addInfo.postValue(repository.addEvent(title,content,date,type))
        },{
            Toast.makeText(TodoApplication.context,it.message, Toast.LENGTH_LONG).show()
        })
    }

    fun updateEvent(id:Int,title: String,content: String,date: String?,status:Int,type: Int){
        launch({
            updateInfo.postValue(repository.updateEvent(id, title, content, date, status, type))
        },{
            Toast.makeText(TodoApplication.context,it.message, Toast.LENGTH_LONG).show()
        })
    }

    private fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        }
    }


}


