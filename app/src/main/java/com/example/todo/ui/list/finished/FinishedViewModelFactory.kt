package com.example.todo.ui.list.finished

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo.data.EventRepository
import com.example.todo.ui.list.unfinished.UnfinishedListViewModel

class FinishedViewModelFactory(private val repository: EventRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FinishedListViewModel(repository) as T
    }

}