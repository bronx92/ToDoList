package com.bronx92.todolist.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bronx92.todolist.databinding.ActivityMainBinding
import com.bronx92.todolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }
    private var register = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK) {
            binding.rvTasks.adapter = adapter
        }
        updateState()
        result.data
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        updateState()
    }

    private fun setListeners() {
        binding.fab.setOnClickListener {
            //startActivity(Intent(this, AddTaskActivity::class.java))
            register.launch(Intent(this, AddTaskActivity::class.java))
        }
        adapter.listenerEdit = {
            register.launch(Intent(this, AddTaskActivity::class.java).apply {
                putExtra(AddTaskActivity.TASK_ID, it.id)
            })
            updateState()
        }
        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            adapter.submitList(TaskDataSource.getList())
            updateState()
        }
    }

    private fun updateState() {
        val list = TaskDataSource.getList()
        if (list.isEmpty()){
            binding.includeState.emptyState.visibility = View.VISIBLE

        } else {
            binding.includeState.emptyState.visibility = View.GONE
        }
        adapter.submitList(list)
    }

}