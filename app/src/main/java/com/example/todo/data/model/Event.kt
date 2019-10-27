package com.example.todo.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Event_table")
class Event {
    @PrimaryKey
    var id: Int = 0

    @NonNull
    @ColumnInfo(name = "title")
    var title: String = ""

    @NonNull
    @ColumnInfo(name = "content")
    var content: String = ""

    @ColumnInfo(name = "date")
    var date: String = ""

    @ColumnInfo(name = "type")
    var type: Int = 0

    @ColumnInfo(name = "priority")
    var priority: Int = 0

    @ColumnInfo(name = "status")
    var status: Int = 0
}