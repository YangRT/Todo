package com.example.todo.ui.list.unfinished

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo.data.EventRepository
import com.example.todo.ui.register.RegisterViewModel

class UnfinishedViewModelFactory(private val repository: EventRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UnfinishedListViewModel(repository) as T
    }
}