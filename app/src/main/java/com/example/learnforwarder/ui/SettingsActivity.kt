package com.example.learnforwarder.ui

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber
import com.example.learnforwarder.R

/**
 * 设置Activity
 */
class SettingsActivity : AppCompatActivity() {
    
    private lateinit var notificationCheckbox: CheckBox
    private lateinit var fileCheckbox: CheckBox
    private lateinit var saveBtn: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        Timber.d("SettingsActivity created")
        
        initViews()
        loadSettings()
        setupListeners()
    }
    
    private fun initViews() {
        notificationCheckbox = findViewById(R.id.notification_checkbox)
        fileCheckbox = findViewById(R.id.file_checkbox)
        saveBtn = findViewById(R.id.save_btn)
    }
    
    private fun loadSettings() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        notificationCheckbox.isChecked = prefs.getBoolean("enable_notification", true)
        fileCheckbox.isChecked = prefs.getBoolean("enable_file_tracking", true)
    }
    
    private fun setupListeners() {
        saveBtn.setOnClickListener {
            saveSettings()
        }
    }
    
    private fun saveSettings() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        prefs.edit().apply {
            putBoolean("enable_notification", notificationCheckbox.isChecked)
            putBoolean("enable_file_tracking", fileCheckbox.isChecked)
            apply()
        }
        
        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()
        finish()
    }
}
