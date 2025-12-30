package com.example.learnforwarder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber
import com.example.learnforwarder.service.MainForegroundService

/**
 * 启动广播接收器
 * 在系统启动完成时启动前台服务
 */
class BootReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        
        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED,
            "com.htc.intent.action.QUICKBOOT_POWERON" -> {
                Timber.d("Boot completed, starting service")
                
                // 检查用户是否启用了自启
                val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                if (prefs.getBoolean("enable_auto_start", true)) {
                    MainForegroundService.startService(context)
                }
            }
        }
    }
}
