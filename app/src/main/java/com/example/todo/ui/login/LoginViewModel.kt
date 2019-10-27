package com.example.todo.ui.login

import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.TodoApplication
import com.example.todo.data.LoginRegisterRepository
import com.example.todo.data.model.RequestInfo
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRegisterRepository):ViewModel(){
    var isUsernameFocus = ObservableInt()
    var isPasswordFocus = ObservableInt()
    var isUsernameNotNull = ObservableInt()
    var isPasswordNotNull = ObservableInt()

    var info = MutableLiveData<RequestInfo>()

     fun login(username:String, password:String){
        launch({
            info.postValue(repository.login(username,password))
        },
            {Toast.makeText(TodoApplication.context,it.message,Toast.LENGTH_LONG).show()})
    }
    private fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        }
    }
}