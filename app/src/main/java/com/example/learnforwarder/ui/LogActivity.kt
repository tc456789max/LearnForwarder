package com.example.learnforwarder.ui

import android.os.Bundle
import android.widget.ListView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import timber.log.Timber
import com.example.learnforwarder.R
import com.example.learnforwarder.data.AppDatabase
import kotlinx.coroutines.launch

/**
 * 日志Activity
 */
class LogActivity : AppCompatActivity() {
    
    private lateinit var logListView: ListView
    private lateinit var clearBtn: Button
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var database: AppDatabase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        
        Timber.d("LogActivity created")
        
        database = AppDatabase.getInstance(this)
        
        initViews()
        setupListeners()
        loadLogs()
    }
    
    private fun initViews() {
        logListView = findViewById(R.id.log_list)
        clearBtn = findViewById(R.id.clear_btn)
        
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf<String>()
        )
        logListView.adapter = adapter
    }
    
    private fun setupListeners() {
        clearBtn.setOnClickListener {
            clearLogs()
        }
    }
    
    private fun loadLogs() {
        lifecycleScope.launch {
            try {
                val notifications = database.notificationDao().getLatest(100)
                val logItems = mutableListOf<String>()
                
                for (notification in notifications) {
                    val item = "${notification.packageName}: ${notification.title} - ${notification.text}"
                    logItems.add(item)
                }
                
                adapter.clear()
                adapter.addAll(logItems)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Timber.e(e, "Error loading logs")
            }
        }
    }
    
    private fun clearLogs() {
        lifecycleScope.launch {
            try {
                database.notificationDao().deleteOlderThan(System.currentTimeMillis())
                adapter.clear()
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Timber.e(e, "Error clearing logs")
            }
        }
    }
}
