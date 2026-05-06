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

        // 1. اختيار الذكر
        val playbackMode = ZekrPrefs.getPlaybackMode(context)
        val zekr = if (playbackMode == 1) {
            val index = ZekrPrefs.getRepeatIndex(context)
            if (index < ZekrData.zekrList.size) ZekrData.zekrList[index]
            else ZekrData.zekrList[0]
        } else {
            val index = ZekrPrefs.nextZekrIndex(context)
            ZekrData.zekrList[index]
        }

        // 2. لو الذكر ده معهوش صوت، تخطى
        if (zekr.audio.isEmpty()) return

        // 3. جيب الـ resource ID من اسم الملف
        val resId = context.resources.getIdentifier(
            zekr.audio, "raw", context.packageName
        )
        if (resId == 0) return

        // 4. شغل الصوت بالمستوى المحفوظ من السلايدر
        try {
            val volume = ZekrPrefs.getVolume(context) // 0.0f - 1.0f
            val mediaPlayer = MediaPlayer.create(context, resId)
            mediaPlayer?.apply {
                setVolume(volume, volume) // ← هنا السلايدر بيأثر فعلاً
                setOnCompletionListener { release() }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
