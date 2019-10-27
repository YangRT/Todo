package com.example.todo.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todo.data.model.Event

@Dao
public interface EventDao{
    @Insert
    fun insert(e: Event)

    @Query("SELECT * FROM EVENT_TABLE WHERE status = 0")
    fun getUnfinishedEvent():LiveData<List<Event>>

    @Query("SELECT * FROM Event_table WHERE status = 1")
    fun getFinishedEvent():LiveData<List<Event>>

    @Delete
    fun delete(e: Event)

    @Query("DELETE From Event_table")
    fun deleteAll()

}