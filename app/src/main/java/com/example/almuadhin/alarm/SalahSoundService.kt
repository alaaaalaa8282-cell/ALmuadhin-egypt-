package com.example.almuadhin.alarm

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.almuadhin.data.SettingsRepository
import com.example.almuadhin.data.UserSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SalahSoundService : Service() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    companion object {
        var salahPlayer: MediaPlayer? = null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val settings: UserSettings = runBlocking {
            settingsRepository.settingsFlow.first()
        }
        val soundResId: Int = settings.salahSound.resId

        salahPlayer?.release()
        salahPlayer = MediaPlayer.create(this, soundResId)
        salahPlayer?.start()
        salahPlayer?.setOnCompletionListener {
            it.release()
            salahPlayer = null
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        salahPlayer?.release()
        salahPlayer = null
        super.onDestroy()
    }
}
