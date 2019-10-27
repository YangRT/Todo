package com.example.todo.ui.list.unfinished

import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.TodoApplication
import com.example.todo.data.EventRepository
import com.example.todo.data.model.Event
import com.example.todo.data.model.EventInfo
import com.example.todo.data.model.RequestInfo
import kotlinx.coroutines.launch

class UnfinishedListViewModel(private val repository:EventRepository):ViewModel(){
    var info = MutableLiveData<EventInfo>()
    var isFirst = ObservableBoolean()
    var isRefreshing = ObservableBoolean()
    var deleteInfo = MutableLiveData<RequestInfo>()
    var updateInfo = MutableLiveData<RequestInfo>()

    fun getInfo(page:Int,status:Int?,type:Int?,orderby:Int?){
        launch({
            info.postValue(repository.getEvent(page,status,type,null,orderby))

        },{
            Toast.makeText(TodoApplication.context,it.message, Toast.LENGTH_LONG).show()
        })
    }




    fun deleteEvent(id:Int){
        launch({
            deleteInfo.postValue(repository.deleteEvent(id))
        },{
            Toast.makeText(TodoApplication.context,it.message, Toast.LENGTH_LONG).show()
        })
    }

    fun updateEvent(id:Int){
        launch({
            updateInfo.postValue(repository.updateStatus(id))
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