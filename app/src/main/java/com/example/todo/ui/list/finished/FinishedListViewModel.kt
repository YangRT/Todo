package com.example.todo.ui.list.finished

import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.TodoApplication
import com.example.todo.data.EventRepository
import com.example.todo.data.model.EventInfo
import com.example.todo.data.model.RequestInfo
import kotlinx.coroutines.launch

class FinishedListViewModel(private val repository: EventRepository):ViewModel() {
    var info = MutableLiveData<EventInfo>()
    var deleteInfo = MutableLiveData<RequestInfo>()

    fun getInfo(page:Int,status:Int?,type:Int?,orderby:Int?){
        launch({
            info.postValue(repository.getEvent(page,status,type,null,null))
        },{
            Toast.makeText(TodoApplication.context,it.message, Toast.LENGTH_LONG).show()
    })
    }

    fun deleteInfo(id:Int){
        launch({
            deleteInfo.postValue(repository.deleteEvent(id))
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