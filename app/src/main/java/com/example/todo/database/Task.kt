package com.example.todo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class Task (
    @PrimaryKey
    val id:String=UUID.randomUUID().toString(),
    val title:String,
    val description:String,
    val isCompleted:Boolean=false
    )