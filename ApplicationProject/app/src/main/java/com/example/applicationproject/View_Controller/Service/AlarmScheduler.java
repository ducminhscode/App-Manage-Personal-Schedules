package com.example.applicationproject.View_Controller.Service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;

public class AlarmScheduler {

    /**
     * Schedule a reminder alarm at the specified time for the given task.
     *
     * @param context Local application or activity context

     * @param reminderTask Uri referencing the task in the content provider
     */

    private static final Map<Integer, PendingIntent> pendingIntentMap = new HashMap<>();

    @SuppressLint("ScheduleExactAlarm")
    public void setAlarm(Context context, long alarmTime, int reminderTask, int idNotify) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context, reminderTask, idNotify);


        pendingIntentMap.put(idNotify, operation);

        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation);

    }

    public void setRepeatAlarm(Context context, long alarmTime, int reminderTask, long RepeatTime, int idNotify) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context, reminderTask, idNotify);

        pendingIntentMap.put( idNotify, operation);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation);


    }

    public void cancelAlarm(Context context, int reminderTask, int idNotify) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context, reminderTask, idNotify);

        if (operation != null) {
            // Hủy bỏ báo thức
            manager.cancel(operation);
            // Xóa PendingIntent khỏi map
            pendingIntentMap.remove(idNotify, operation);
        }

    }

    public void cancelAllAlarms(Context context) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        for (Map.Entry<Integer, PendingIntent> entry : pendingIntentMap.entrySet()) {
            manager.cancel(entry.getValue());
        }

        pendingIntentMap.clear();
    }

}
