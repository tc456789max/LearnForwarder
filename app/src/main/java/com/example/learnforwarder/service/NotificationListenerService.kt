package com.example.learnforwarder.service

import android.app.Notification
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.annotation.RequiresApi
import timber.log.Timber
import com.example.learnforwarder.data.NotificationRecord
import com.example.learnforwarder.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 通知监听服务
 * 用于监听系统通知栏的消息
 */
class NotificationListenerService : NotificationListenerService() {
    
    private val scope = CoroutineScope(Dispatchers.Default)
    private lateinit var database: AppDatabase
    
    override fun onCreate() {
        super.onCreate()
        Timber.d("NotificationListenerService created")
        database = AppDatabase.getInstance(this)
    }
    
    override fun onListenerConnected() {
        super.onListenerConnected()
        Timber.d("NotificationListenerService connected")
        
        // 获取当前所有通知
        val notifications = activeNotifications
        Timber.d("Current active notifications: ${notifications?.size ?: 0}")
    }
    
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        
        if (sbn == null) return
        
        try {
            val notification = sbn.notification
            val packageName = sbn.packageName
            val key = sbn.key
            val postTime = sbn.postTime
            
            // 提取通知内容
            val title = extractTitle(notification)
            val text = extractText(notification)
            
            Timber.d("Notification posted: package=$packageName, title=$title, text=$text")
            
            // 保存到数据库
            val record = NotificationRecord(
                packageName = packageName,
                title = title,
                text = text,
                timestamp = System.currentTimeMillis(),
                key = key
            )
            
            scope.launch {
                database.notificationDao().insert(record)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error processing notification")
        }
    }
    
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        
        if (sbn == null) return
        
        try {
            val packageName = sbn.packageName
            val key = sbn.key
            
            Timber.d("Notification removed: package=$packageName, key=$key")
        } catch (e: Exception) {
            Timber.e(e, "Error processing removed notification")
        }
    }
    
    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Timber.d("NotificationListenerService disconnected")
    }
    
    /**
     * 从通知中提取标题
     */
    private fun extractTitle(notification: Notification): String {
        return try {
            notification.extras?.getCharSequence(Notification.EXTRA_TITLE)?.toString() ?: ""
        } catch (e: Exception) {
            ""
        }
    }
    
    /**
     * 从通知中提取文本内容
     */
    private fun extractText(notification: Notification): String {
        return try {
            notification.extras?.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
        } catch (e: Exception) {
            ""
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Timber.d("NotificationListenerService destroyed")
    }
}
