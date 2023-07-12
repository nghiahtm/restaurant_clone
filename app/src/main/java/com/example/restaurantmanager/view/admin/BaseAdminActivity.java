package com.example.restaurantmanager.view.admin;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.utils.ProgressDialog;
import com.example.restaurantmanager.utils.RealmUtility;

import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmAsyncTask;

public class BaseAdminActivity  extends AppCompatActivity {
    public FragmentManager mSupportFragmentManager;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    Realm realm;
    RealmAsyncTask transaction;
    ProgressDialog dialog;


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSupportFragmentManager = getSupportFragmentManager();
        context = this;
        showBackButton(true);
        Realm.init(context);
        realm = Realm.getInstance(RealmUtility.getDefaultConfig());
        dialog = new ProgressDialog(context);

    }

    ////////////////////////////////////////////////////////
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
    /////////////////////////////////////////////////////



    public static Context getContext() {
        return context;
    }

    protected void showBackButton(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        }
    }

    public void setActionbarTitle(@Nullable String title) {
        if (title == null) {
            return;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

}
