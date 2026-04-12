package com.example.almuadhin.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.almuadhin.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SalahReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, SalahSoundService::class.java)
        context.startService(serviceIntent)

        val notif = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_AZKAR)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("الصلاة على النبي ﷺ")
            .setContentText("اللهم صل وسلم على نبينا محمد")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(9001, notif)
    }
}
