package com.example.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todo.database.Task
import com.example.todo.database.TaskDao
import com.example.todo.database.TaskDb
import com.example.todo.repo.TaskRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel( application: Application):AndroidViewModel(application) {
    // not using hilt or dagger just simple implementation for the data

    private val database:TaskDb = TaskDb.getInstance(application)
    private val taskRepo:TaskRepo= TaskRepo(database.getTaskDao())


    private val _tasks:MutableStateFlow<List<Task>> = MutableStateFlow<List<Task>>(emptyList())
    val tasks:StateFlow<List<Task>> = _tasks

    init{
        getAllTask()
    }

    private fun getAllTask() {
         viewModelScope.launch{
            _tasks.value= taskRepo.getAllTask()
         }
    }
    fun addTask(task:Task) {
         viewModelScope.launch{
             taskRepo.addTask(task)
             getAllTask()
         }
    }
    fun deleteTask(id:String) {
         viewModelScope.launch{
             taskRepo.deleteTask(id)
             getAllTask()
         }
    }
    fun completeTask(id:String,isCompleted:Boolean){
        viewModelScope.launch{
            taskRepo.completeTask(id,isCompleted)
            getAllTask()
        }
    }

}

class TaskViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}