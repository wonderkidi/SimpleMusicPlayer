package com.hk88.simplemusicplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.Toast

class MusicPlayerService : Service() {

    var mMediaPlayer : MediaPlayer? = null
    var mBinder : MusicPlayerBinder = MusicPlayerBinder()
    inner class MusicPlayerBinder: Binder() {
        fun getService(): MusicPlayerService {
            return this@MusicPlayerService
        }
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    fun startForegroundService() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE)
                as NotificationManager
            val mChannel = NotificationChannel(
                "CHANNEL_ID",
                "CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(mChannel)
        }
        val notification: Notification = Notification.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_play)
            .setContentTitle("음악앱")
            .setContentText("앱실행중")
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }
    }

    fun isPlaying() :Boolean {
        return (mMediaPlayer != null && mMediaPlayer?.isPlaying ?: false)
    }

    fun play() {
        if(mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.chocolate)
            mMediaPlayer?.setVolume(1.0f, 1.0f)
            mMediaPlayer?.isLooping = true
            mMediaPlayer?.start()
        } else {
            if(mMediaPlayer!!.isPlaying) {
                Toast.makeText(this, "이미 음악 실행중", Toast.LENGTH_SHORT).show()
            } else {
                mMediaPlayer?.start()
            }
        }
    }

    fun pause() {
        mMediaPlayer?.let {
            if(it.isPlaying) {
                it.pause()
            }
        }
    }

    fun stop() {
        mMediaPlayer?.let {
            if(it.isPlaying) {
                it.stop()
                it.release()
                mMediaPlayer = null
            }
        }
    }
}