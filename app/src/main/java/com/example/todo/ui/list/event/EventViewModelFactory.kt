package com.example.todo.ui.list.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo.data.EventRepository

class EventViewModelFactory(private val repository: EventRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EventViewModel(repository) as T
    }
}