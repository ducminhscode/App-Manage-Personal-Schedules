package com.example.applicationproject.View_Controller.Service;

import static com.example.applicationproject.Application.MyApplication.CHANEL_ID;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.example.applicationproject.Application.MyApplication;
import com.example.applicationproject.Database.ToDoDBContract;
import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.Model.Ringtone;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Activity.DetailMissionActivity;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.FragmentLogin.MeFragment;
import com.example.applicationproject.View_Controller.FragmentLogin.MissionFragment;
import com.example.applicationproject.View_Controller.ScreenMainLogin;
import com.example.applicationproject.View_Controller.ScreenMainNoLogin;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.LogRecord;

public class ReminderAlarmService extends IntentService implements TextToSpeech.OnInitListener {

    private static final String TAG = ReminderAlarmService.class.getSimpleName();
    private static final long TIMEOUT_MS = 5000;
    private TextToSpeech textToSpeech;
    private static final int NOTIFICATION_ID = 42;
    private Mission mainList;
    private List<Ringtone> ringtoneList;

    public static PendingIntent getReminderPendingIntent(Context context, int uri, int idNotify) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.putExtra("id_mission", uri);
        action.putExtra("idNotify", idNotify);
        return PendingIntent.getService(context, idNotify, action, PendingIntent.FLAG_IMMUTABLE);
    }

    public ReminderAlarmService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo TextToSpeech trong onCreate() để tránh việc khởi tạo lại nhiều lần
        textToSpeech = new TextToSpeech(this, this);
    }

    private void getRingtoneList(List<Ringtone> ringtoneList) {
        Cursor cursor = DAO.getAllRingToneList(getBaseContext());
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDBContract.RingtoneEntry.RINGTONE_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDBContract.RingtoneEntry.RINGTONE_TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDBContract.RingtoneEntry.RINGTONE_PATH));
                Ringtone ringtone = new Ringtone(id, title, path);
                ringtoneList.add(ringtone);
            }
            cursor.close();
        }
    }

    private void getMainList(Mission mainList, int id) {
        Cursor cursor = DAO.getAllMission(getBaseContext(), id);
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String isSticker = cursor.getString(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_isSTICKER));
                @SuppressLint("Range") int sticker_id = cursor.getInt(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID));
                @SuppressLint("Range") int category_id = cursor.getInt(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_CATEGORY_ID));
                @SuppressLint("Range") int mission_id = cursor.getInt(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_ID));
                @SuppressLint("Range") int ringTone_id = cursor.getInt(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_DATE));
                @SuppressLint("Range") String describe = cursor.getString(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_DESCRIPTION));
                @SuppressLint("Range") String isNotify = cursor.getString(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_isNOTIFY));
                @SuppressLint("Range") String isRepeat = cursor.getString(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_isREPEAT));
                @SuppressLint("Range") String repeatType = cursor.getString(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_REPEAT_TYPE));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_TIME));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_TITLE));
                @SuppressLint("Range") String repeatNo = cursor.getString(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_REPEAT_NO));
                @SuppressLint("Range") String reminder = cursor.getString(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_REMINDER));
                @SuppressLint("Range") String reminderType = cursor.getString(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_REMINDER_TYPE));
                @SuppressLint("Range") String isActive = cursor.getString(cursor.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_isACTIVE));
                mainList = new Mission(sticker_id, ringTone_id, date, describe, isNotify, isRepeat, repeatType, mission_id, time, title, category_id, repeatNo, reminder, reminderType, isSticker, isActive);
            }
            cursor.close();
        }
    }

    @SuppressLint("WrongThread")
    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert intent != null;
        int uri = intent.getIntExtra("id_mission", 0);
        int idNotify = intent.getIntExtra("idNotify", 0);

        getMainList(mainList, uri);
        getRingtoneList(ringtoneList);

        // Xử lý thông tin về nhiệm vụ
        List<Mission> missionList = new ArrayList<>();
        missionList.add(new Mission(1, 1, "29/12/2025", "Complete the project report", "true", "true", "daily", 1, "19:00", "Morning meeting", 1, "5", "10 minutes before", "time", "true", "true"));
        missionList.add(new Mission(1, 1, "28/11/2025", "Complete the project report", "true", "true", "daily", 2, "10:00", "Morning meeting", 1, "5", "10 minutes before", "time", "true", "true"));
        missionList.add(new Mission(1, 1, "27/10/2025", "Complete the project report", "true", "true", "daily", 3, "11:00", "Morning meeting", 2, "5", "10 minutes before", "time", "true", "true"));
        missionList.add(new Mission(1, 1, "26/09/2025", "Complete the project report", "true", "true", "daily", 4, "02:00", "Morning meeting", 2, "5", "10 minutes before", "time", "true", "true"));
        missionList.add(new Mission(1, 1, "25/07/2025", "Complete the project report", "true", "true", "daily", 5, "09:00", "Morning meeting", 3, "5", "10 minutes before", "time", "true", "true"));
        missionList.add(new Mission(1, 1, "28/06/2025", "Complete the project report", "true", "true", "daily", 6, "13:00", "Morning meeting", 3, "5", "10 minutes before", "time", "true", "true"));

        String description = missionList.stream()
                .filter(v -> v.getMission_id() == uri)
                .findFirst().map(Mission::getTitle).orElse("");

        // Xử lý âm thanh báo thức
        String ringTone = getRingTone(uri, missionList);

        // Tạo thông báo
        Intent action = new Intent(this, ScreenMainLogin.class);
        action.putExtra("id_mission", uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(idNotify, PendingIntent.FLAG_IMMUTABLE);

        String typeofReminder = missionList.stream()
                .filter(v -> v.getMission_id() == uri)
                .findFirst().map(Mission::getReminderType).orElse("");

        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (!ringTone.equals("Không")) {
            notificationUri = Uri.parse(ringTone);
        }

        // Hiển thị thông báo hoặc phát âm
        if (typeofReminder.equals("Thông báo")) {
            showNotification(manager, description, operation, notificationUri);
        } else {
            // Phát âm nếu là báo thức
            String textToRead = "Alarm you have a mission";
            speakNotification(textToRead);
            showNotification(manager, description, operation, notificationUri);
        }
    }

    private String getRingTone(int uri, List<Mission> missionList) {
        String ringTone = "";
        if (ringtoneList == null) {
            ringTone = "Không";
            return ringTone;
        }
        ringTone = ringtoneList.stream().filter(v -> v.getRingTone_id() == missionList.stream()
                .filter(r -> r.getMission_id() == uri).findFirst().get().getRingTone_id())
                .findFirst().map(Ringtone::getRingTone_path).orElse("Không");
        return ringTone;
    }

    private void showNotification(NotificationManager manager, String description, PendingIntent operation, Uri notificationUri) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.notification);

        Notification note = new NotificationCompat.Builder(this, CHANEL_ID)
                .setContentTitle("Thông báo")
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(bitmap)
                .setContentIntent(operation)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(notificationUri)
                .setAutoCancel(true)
                .build();

        manager.notify(NOTIFICATION_ID, note);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            manager.cancel(NOTIFICATION_ID);
            if (textToSpeech != null) {
                textToSpeech.stop();
                textToSpeech.shutdown();
            }
        }, TIMEOUT_MS);
    }

    private void speakNotification(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int langResult = textToSpeech.setLanguage(Locale.US);
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language is not supported or missing data.");
            }
        } else {
            Log.e(TAG, "TextToSpeech initialization failed.");
        }
    }
}

