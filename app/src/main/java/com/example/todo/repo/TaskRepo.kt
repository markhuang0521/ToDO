package com.example.todo.repo

import com.example.todo.database.Task
import com.example.todo.database.TaskDao

class TaskRepo(private val taskDao: TaskDao) {

    suspend fun getAllTask():List<Task> = taskDao.getAllTask()

    suspend fun addTask(task:Task) = taskDao.addTask(task)

    suspend fun deleteTask(id: String) = taskDao.deleteTask(id)
    suspend fun completeTask(id:String, isCompleted:Boolean) = taskDao.completeTask(id,isCompleted)

}