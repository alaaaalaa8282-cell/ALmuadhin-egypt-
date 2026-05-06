package com.example.almuadhin.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.speech.tts.TextToSpeech
import com.example.almuadhin.data.ZekrData
import com.example.almuadhin.data.ZekrPrefs
import java.util.Locale

class ZekrReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (!ZekrPrefs.isEnabled(context)) return

        // 1. تحديد النص المراد قراءته
        val playbackMode = ZekrPrefs.getPlaybackMode(context)
        val zekrText = if (playbackMode == 1) {
            // تكرار ذكر محدد
            val index = ZekrPrefs.getRepeatIndex(context)
            if (index < ZekrData.zekrList.size)
                ZekrData.zekrList[index].name
            else
                ZekrData.zekrList[0].name
        } else {
            // تسلسل (دور)
            val index = ZekrPrefs.nextZekrIndex(context)
            ZekrData.zekrList[index].name
        }

        // 2. ضبط مستوى الصوت
        val volume = ZekrPrefs.getVolume(context) // 0.0f - 1.0f
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val targetVolume = (volume * maxVolume).toInt()
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            targetVolume,
            0
        )

        // 3. تشغيل TTS
        var tts: TextToSpeech? = null
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("ar")
                tts?.speak(
                    zekrText,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "zekr_utterance"
                )
            }
        }
    }
}
