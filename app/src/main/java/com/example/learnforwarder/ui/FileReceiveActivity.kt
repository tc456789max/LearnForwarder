package com.example.learnforwarder.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import timber.log.Timber
import com.example.learnforwarder.R
import com.example.learnforwarder.data.AppDatabase
import com.example.learnforwarder.data.FileRecord
import kotlinx.coroutines.launch

/**
 * 文件接收Activity
 * 处理通过ACTION_SEND分享的文件
 */
class FileReceiveActivity : AppCompatActivity() {
    
    private lateinit var infoText: TextView
    private lateinit var database: AppDatabase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_receive)
        
        Timber.d("FileReceiveActivity created")
        
        infoText = findViewById(R.id.info_text)
        database = AppDatabase.getInstance(this)
        
        handleIntent(intent)
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
    
    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SEND -> {
                handleSingleFile(intent)
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                handleMultipleFiles(intent)
            }
            else -> {
                Timber.d("Unknown action: ${intent.action}")
            }
        }
    }
    
    private fun handleSingleFile(intent: Intent) {
        val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        val type = intent.type
        
        if (uri != null) {
            Timber.d("Received file: uri=$uri, type=$type")
            
            val fileName = getFileName(uri)
            val fileSize = getFileSize(uri)
            
            val info = "File Received:\nName: $fileName\nSize: $fileSize bytes\nType: $type"
            infoText.text = info
            
            // 保存到数据库
            saveFileRecord(fileName, uri.toString(), fileSize, type ?: "*/*")
            
            Toast.makeText(this, "File received: $fileName", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun handleMultipleFiles(intent: Intent) {
        val uris = intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
        
        if (uris != null && uris.isNotEmpty()) {
            Timber.d("Received ${uris.size} files")
            
            val info = StringBuilder("Files Received:\n")
            for ((index, uri) in uris.withIndex()) {
                val fileName = getFileName(uri)
                val fileSize = getFileSize(uri)
                
                info.append("${index + 1}. $fileName ($fileSize bytes)\n")
                
                // 保存到数据库
                saveFileRecord(fileName, uri.toString(), fileSize, "*/*")
            }
            
            infoText.text = info.toString()
            Toast.makeText(this, "Received ${uris.size} files", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun getFileName(uri: Uri): String {
        return try {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex("_display_name")
                    if (nameIndex >= 0) {
                        return@use it.getString(nameIndex)
                    }
                }
            }
            uri.lastPathSegment ?: "unknown"
        } catch (e: Exception) {
            Timber.e(e, "Error getting file name")
            "unknown"
        }
    }
    
    private fun getFileSize(uri: Uri): Long {
        return try {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val sizeIndex = it.getColumnIndex("_size")
                    if (sizeIndex >= 0) {
                        return@use it.getLong(sizeIndex)
                    }
                }
            }
            0L
        } catch (e: Exception) {
            Timber.e(e, "Error getting file size")
            0L
        }
    }
    
    private fun saveFileRecord(
        fileName: String,
        filePath: String,
        fileSize: Long,
        mimeType: String
    ) {
        lifecycleScope.launch {
            try {
                val record = FileRecord(
                    fileName = fileName,
                    filePath = filePath,
                    fileSize = fileSize,
                    mimeType = mimeType,
                    timestamp = System.currentTimeMillis()
                )
                database.fileDao().insert(record)
            } catch (e: Exception) {
                Timber.e(e, "Error saving file record")
            }
        }
    }
}
