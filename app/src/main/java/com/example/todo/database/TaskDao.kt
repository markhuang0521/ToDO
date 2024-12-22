package com.example.todo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {
    @Insert()
    suspend fun addTask(task: Task)

    @Query("SELECT * FROM tasks")
    suspend fun getAllTask():List<Task>
    @Query("UPDATE tasks SET isCompleted= :isCompleted  where id=:id")
    suspend fun completeTask(id: String,isCompleted:Boolean)
    @Query("DELETE FROM tasks where id=:id")
    suspend fun deleteTask(id: String)
}