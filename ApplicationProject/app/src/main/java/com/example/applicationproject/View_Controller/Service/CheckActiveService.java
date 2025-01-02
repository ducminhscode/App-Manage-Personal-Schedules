package com.example.applicationproject.View_Controller.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.applicationproject.View_Controller.FragmentLogin.MissionFragment;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class CheckActiveService extends Service {

    ScheduledExecutorService executorService;
    private final IBinder binder = new LocalBinder();
    private Context context;

    public class LocalBinder extends Binder {
        public CheckActiveService getService() {
            return CheckActiveService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable checkTasksRunnable = new Runnable() {
            @Override
            public void run() {
                MissionFragment.checkActive(context, MissionFragment.shareDataDialogs);
            }
        };
        executorService.scheduleAtFixedRate(checkTasksRunnable, 0, 1, TimeUnit.MINUTES);
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        executorService.shutdown();
        return super.onUnbind(intent);
    }
}
