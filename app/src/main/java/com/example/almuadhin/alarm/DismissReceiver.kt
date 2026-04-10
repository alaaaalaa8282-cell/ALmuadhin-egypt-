package com.example.almuadhin.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class DismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notifId = intent.getIntExtra(AlarmReceiver.EXTRA_ID, 1001)
        NotificationManagerCompat.from(context).cancel(notifId)
    }
}
