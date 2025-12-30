package com.example.learnforwarder.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Delete
import android.content.Context
import androidx.room.Room

/**
 * 通知记录数据模型
 */
@Entity(tableName = "notifications")
data class NotificationRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val packageName: String,
    val title: String,
    val text: String,
    val timestamp: Long,
    val key: String
)

/**
 * 文件记录数据模型
 */
@Entity(tableName = "files")
data class FileRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fileName: String,
    val filePath: String,
    val fileSize: Long,
    val mimeType: String,
    val timestamp: Long
)

/**
 * 通知DAO
 */
@Dao
interface NotificationDao {
    @Insert
    suspend fun insert(notification: NotificationRecord): Long
    
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getLatest(limit: Int = 100): List<NotificationRecord>
    
    @Query("SELECT * FROM notifications WHERE packageName = :packageName ORDER BY timestamp DESC")
    suspend fun getByPackage(packageName: String): List<NotificationRecord>
    
    @Query("DELETE FROM notifications WHERE timestamp < :beforeTime")
    suspend fun deleteOlderThan(beforeTime: Long)
    
    @Delete
    suspend fun delete(notification: NotificationRecord)
}

/**
 * 文件DAO
 */
@Dao
interface FileDao {
    @Insert
    suspend fun insert(file: FileRecord): Long
    
    @Query("SELECT * FROM files ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getLatest(limit: Int = 100): List<FileRecord>
    
    @Query("DELETE FROM files WHERE timestamp < :beforeTime")
    suspend fun deleteOlderThan(beforeTime: Long)
    
    @Delete
    suspend fun delete(file: FileRecord)
}

/**
 * 应用数据库
 */
@Database(
    entities = [NotificationRecord::class, FileRecord::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
    abstract fun fileDao(): FileDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "learnforwarder.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
