package com.example.learnforwarder.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import timber.log.Timber
import com.example.learnforwarder.R
import com.example.learnforwarder.service.MainForegroundService

/**
 * 主Activity
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var statusText: TextView
    private lateinit var startServiceBtn: Button
    private lateinit var stopServiceBtn: Button
    private lateinit var settingsBtn: Button
    private lateinit var logsBtn: Button
    
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var allGranted = true
        for ((_, granted) in permissions) {
            if (!granted) {
                allGranted = false
                break
            }
        }
        
        if (allGranted) {
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
            startService()
        } else {
            Toast.makeText(this, "Some permissions denied", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        Timber.d("MainActivity created")
        
        initViews()
        setupListeners()
        checkNotificationAccess()
    }
    
    private fun initViews() {
        statusText = findViewById(R.id.status_text)
        startServiceBtn = findViewById(R.id.start_service_btn)
        stopServiceBtn = findViewById(R.id.stop_service_btn)
        settingsBtn = findViewById(R.id.settings_btn)
        logsBtn = findViewById(R.id.logs_btn)
    }
    
    private fun setupListeners() {
        startServiceBtn.setOnClickListener {
            requestPermissionsAndStart()
        }
        
        stopServiceBtn.setOnClickListener {
            stopService()
        }
        
        settingsBtn.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        
        logsBtn.setOnClickListener {
            startActivity(Intent(this, LogActivity::class.java))
        }
    }
    
    private fun checkNotificationAccess() {
        val enabled = isNotificationAccessEnabled()
        updateStatus(enabled)
    }
    
    private fun isNotificationAccessEnabled(): Boolean {
        val contentResolver = contentResolver
        val enabledNotificationListeners = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        )
        val packageName = packageName
        
        return enabledNotificationListeners?.contains(packageName) ?: false
    }
    
    private fun requestPermissionsAndStart() {
        val permissions = mutableListOf<String>()
        
        // 检查存储权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ 使用 MANAGE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            }
        } else {
            // Android 10 及以下使用传统权限
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
        
        if (permissions.isNotEmpty()) {
            permissionLauncher.launch(permissions.toTypedArray())
        } else {
            startService()
        }
    }
    
    private fun startService() {
        // 检查通知访问权限
        if (!isNotificationAccessEnabled()) {
            Toast.makeText(
                this,
                "Please enable notification access in settings",
                Toast.LENGTH_LONG
            ).show()
            
            // 打开通知访问设置
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            return
        }
        
        MainForegroundService.startService(this)
        updateStatus(true)
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show()
    }
    
    private fun stopService() {
        MainForegroundService.stopService(this)
        updateStatus(false)
        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show()
    }
    
    private fun updateStatus(running: Boolean) {
        statusText.text = if (running) {
            "Status: Service Running"
        } else {
            "Status: Service Stopped"
        }
    }
}
