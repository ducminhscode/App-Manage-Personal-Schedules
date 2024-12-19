package com.example.applicationproject.View_Controller.BroadCastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReminderAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int reminderTask = intent.getIntExtra("reminderTask", -1);
        if (reminderTask != -1) {
            Toast.makeText(context, "Reminder task: " + reminderTask, Toast.LENGTH_LONG).show();
        }
    }
}
