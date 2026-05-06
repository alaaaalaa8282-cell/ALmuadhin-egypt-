package com.example.almuadhin.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
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
            val mediaPlayer = MediaPlayer.create(context, zekr.resId)
            mediaPlayer?.apply {
                setVolume(volume, volume)
                setOnCompletionListener { release() }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // ← إعادة الجدولة للمرة الجاية
        val intervalMinutes = ZekrPrefs.getIntervalInMinutes(context)
        ZekrScheduler.schedule(context, intervalMinutes.toLong())
    }
}
