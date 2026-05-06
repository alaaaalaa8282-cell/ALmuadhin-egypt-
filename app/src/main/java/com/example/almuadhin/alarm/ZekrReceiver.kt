package com.example.almuadhin.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import com.example.almuadhin.data.ZekrData
import com.example.almuadhin.data.ZekrPrefs

class ZekrReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (!ZekrPrefs.isEnabled(context)) return

        val playbackMode = ZekrPrefs.getPlaybackMode(context)
        val zekr = if (playbackMode == 1) {
            val index = ZekrPrefs.getRepeatIndex(context)
            if (index < ZekrData.zekrList.size) ZekrData.zekrList[index]
            else ZekrData.zekrList[0]
        } else {
            val index = ZekrPrefs.nextZekrIndex(context)
            ZekrData.zekrList[index]
        }

        try {
            val volume = ZekrPrefs.getVolume(context)
            // تحويل القيمة لـ logarithmic عشان السلايدر يبان صح
            val logVolume = if (volume == 0f) 0f
                           else (1 - (Math.log((1 + (1 - volume) * 99).toDouble()) / Math.log(100.0))).toFloat()

            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            // طلب AudioFocus عشان الصوت ما يتوقفش
            var focusGranted = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .build()
                focusGranted = audioManager.requestAudioFocus(focusRequest) ==
                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED
            } else {
                @Suppress("DEPRECATION")
                focusGranted = audioManager.requestAudioFocus(
                    null,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
                ) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
            }

            if (!focusGranted) return

            val mediaPlayer = MediaPlayer.create(context, zekr.resId)
            mediaPlayer?.apply {
                setVolume(logVolume, logVolume)
                setOnCompletionListener { release() }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intervalMinutes = ZekrPrefs.getIntervalInMinutes(context)
        ZekrScheduler.schedule(context, intervalMinutes.toLong())
    }
}
