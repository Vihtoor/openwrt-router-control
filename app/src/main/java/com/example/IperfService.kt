package com.example

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import java.util.concurrent.Executors

class IperfService : Service() {

    companion object {
        private const val TAG = "IperfService"
        private const val CHANNEL_ID = "iperf_channel_id"
        private const val NOTIFICATION_ID = 5201
        
        private val executor = Executors.newSingleThreadExecutor()

        fun start(context: Context) {
            val intent = Intent(context, IperfService::class.java)
            context.startService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, IperfService::class.java)
            context.stopService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "IperfService onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "IperfService onStartCommand")
        
        // Start native iperf3 server sequentially off the main thread
        executor.execute {
            IperfServerManager.startServer(applicationContext)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "IperfService onDestroy")
        // Stop native iperf3 server sequentially off the main thread
        executor.execute {
            IperfServerManager.stopServer()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
        }

        return builder
            .setContentTitle("iPerf3 Server Active")
            .setContentText("Listening on port 5201")
            .setSmallIcon(android.R.drawable.stat_notify_sync) // simple platform system sync icon
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "iPerf3 Server Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notification channel for running iPerf3 Server background process"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}
