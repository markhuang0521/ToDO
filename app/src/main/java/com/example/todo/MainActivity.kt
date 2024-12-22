package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.todo.database.Task
import com.example.todo.ui.theme.ToDoTheme

private lateinit var viewModel: TaskViewModel
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=ViewModelProvider(
            this,
            // object : ViewModelProvider.AndroidViewModelFactory(application) {}
        ).get(TaskViewModel::class.java)

        setContent {
            ToDoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TaskScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            Text(text = "Completed Tasks", modifier = Modifier.fillMaxWidth().padding(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(tasks.filter { it.isCompleted }) { task ->
                    TaskItem(task = task,
                        onCompleteClick = { id, isCompleted ->
                            viewModel.completeTask(
                                id,
                                isCompleted
                            )
                        },
                        onDeleteClick = { id -> viewModel.deleteTask(id) })
                }
            }
            Text(text = "Current Tasks", modifier = Modifier.fillMaxWidth().padding(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(tasks.filter { !it.isCompleted }) { task ->
                    TaskItem(task = task,
                        onCompleteClick = { id, isCompleted ->
                            viewModel.completeTask(
                                id,
                                isCompleted
                            )
                        },
                        onDeleteClick = { id -> viewModel.deleteTask(id) })
                }

            }
            if (showDialog) {
                AddTaskDialog(
                    onAddClick = { task -> viewModel.addTask(task) },
                    onDismiss = { showDialog = false })
            }

        }
        // Floating Action Button (FAB) at the bottom right
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd)  // Position the FAB at the bottom right
                .padding(16.dp)  // Add padding to keep the FAB from touching screen edges
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Task")
        }
    }
}
@Composable
fun TaskItem(task:Task,onCompleteClick:(String,Boolean)->Unit, onDeleteClick:(String)->Unit){
    Row(modifier= Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = task.isCompleted, onCheckedChange = {isChecked-> onCompleteClick(task.id,isChecked)})
        Column(modifier=Modifier.weight(1f)){
            Text(text = task.title, style = TextStyle(textDecoration = if(task.isCompleted) TextDecoration.LineThrough else null,),color =if (task.isCompleted) Color.Gray else Color.Black)
            Text(text = task.description,style = TextStyle(textDecoration = if(task.isCompleted) TextDecoration.LineThrough else null,),color=if (task.isCompleted) Color.Gray else Color.Black)
        }
        IconButton(onClick = { onDeleteClick(task.id)}) {
            Icon(imageVector = Icons.Default.Delete , contentDescription = "delete")
        }

    }
}
@Composable
fun AddTaskDialog(onAddClick:(Task)->Unit,onDismiss:()->Unit){
    var title by remember { mutableStateOf("")}
    var description by remember { mutableStateOf("")}

    AlertDialog(onDismissRequest = onDismiss,
        title = { Text(text = "Add New Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

            }
        },
        confirmButton = {
            Button(onClick = {
                onAddClick(Task(title=title, description = description))
                onDismiss()
            }) {
                Text("Add Task")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}
