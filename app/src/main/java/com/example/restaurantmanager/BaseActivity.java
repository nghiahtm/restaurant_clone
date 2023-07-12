package com.example.restaurantmanager;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        // Check if lock task mode is active
        if (isInLockTaskMode()) {
            // Relaunch the activity to prevent exiting lock task mode
            startLockTask();
        } else {
            super.onBackPressed();
        }
    }

    private boolean isInLockTaskMode() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
        if (runningTasks != null && !runningTasks.isEmpty()) {
            ComponentName componentName = runningTasks.get(0).topActivity;
            return componentName.getPackageName().equals(getPackageName());
        }
        return false;
    }
}
