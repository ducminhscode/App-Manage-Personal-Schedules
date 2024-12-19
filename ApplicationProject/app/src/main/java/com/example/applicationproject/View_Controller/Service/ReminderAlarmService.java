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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.Model.Ringtone;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Activity.DetailMissionActivity;
import com.example.applicationproject.View_Controller.FragmentLogin.MeFragment;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.LogRecord;

public class ReminderAlarmService extends IntentService implements TextToSpeech.OnInitListener {

    private static final String TAG = ReminderAlarmService.class.getSimpleName();
    private static final long TIMEOUT_MS = 5000;
    private TextToSpeech textToSpeech;
    private final ShareDataMission shareDataMission = new ViewModelProvider.AndroidViewModelFactory(getApplication())
            .create(ShareDataMission.class);
    private static final int NOTIFICATION_ID = 42;

    public static PendingIntent getReminderPendingIntent(Context context, int uri, int idNotify) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.putExtra("id_mission", uri);
        action.putExtra("idNotify", idNotify);
        return PendingIntent.getService(context, idNotify, action, PendingIntent.FLAG_UPDATE_CURRENT);
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

    @SuppressLint("WrongThread")
    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert intent != null;
        int uri = intent.getIntExtra("id_mission", 0);
        int idNotify = intent.getIntExtra("idNotify", 0);

        // Xử lý thông tin về nhiệm vụ
        List<Mission> missionList = shareDataMission.getMainList().getValue();
        if (missionList == null) {
            missionList = new ArrayList<>();
        }
        String description = missionList.stream()
                .filter(v -> v.getMission_id() == uri)
                .findFirst().map(Mission::getTitle).orElse("");

        // Xử lý âm thanh báo thức
        String ringTone = getRingTone(uri, missionList);

        // Tạo thông báo
        Intent action = new Intent(this, DetailMissionActivity.class);
        action.putExtra("id_mission", uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(idNotify, PendingIntent.FLAG_UPDATE_CURRENT);

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
        List<Ringtone> ringtoneList = shareDataMission.getRingtoneList().getValue();
        if (ringtoneList != null) {
            ringTone = ringtoneList.stream()
                    .filter(v -> v.getRingTone_id() == missionList.stream()
                            .filter(r -> r.getMission_id() == uri)
                            .findFirst().get().getRingTone_id())
                    .findFirst().map(Ringtone::getRingTone_path).orElse("Không");
        }
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

