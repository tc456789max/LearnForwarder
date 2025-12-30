package com.example.learnforwarder

import android.app.Application
import timber.log.Timber
import com.example.learnforwarder.BuildConfig

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // 初始化日志库
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        Timber.d("Application initialized")
    }
}
