package com.guo.duoduo.anyshareofandroid.ui.main;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.guo.duoduo.anyshareofandroid.R;
import com.guo.duoduo.anyshareofandroid.utils.permission.Nammu;
import com.guo.duoduo.anyshareofandroid.utils.permission.PermissionCallback;

import java.util.ArrayList;
import java.util.List;


public class WelcomeActivity extends Activity implements PermissionCallback {
    private static final String TAG = "WelcomeActivity";
    private static final String PACKAGE_URI_PREFIX = "package:";
    private static final ArgbEvaluator ARGB_EVALUATOR = new ArgbEvaluator();
    private static final int HANDLER_MESSAGE_ANIMATION = 0;
    private static final int HANDLER_MESSAGE_NEXT_ACTIVITY = 1;
    private ImageView melon1;
    private View rootView;
    private ColorDrawable colorDrawable;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDLER_MESSAGE_ANIMATION) {
                //playAnimator();
                playColorAnimator();
            } else if (msg.what == HANDLER_MESSAGE_NEXT_ACTIVITY) {
                next();
            }
        }
    };
    private long mRequestTimeMillis;

    private void next() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.scroll_in, R.anim.scroll_out);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initWidget();
    }

    private void initWidget() {
        rootView = findViewById(R.id.welcom_root);
        melon1 = (ImageView) findViewById(R.id.activity_welcome_melon1);
        colorDrawable = new ColorDrawable(Color.BLACK);
        rootView.setBackground(colorDrawable);
        mHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_ANIMATION, 900L);
    }

    private void playColorAnimator() {
        final List<Animator> animList = new ArrayList<>();
        //final int toColor = getResources().getColor(R.color.colorPrimaryDark);
        final int toColor = Color.parseColor("#314D5B");
        final android.view.Window window = getWindow();
        ObjectAnimator statusBarColor = ObjectAnimator.ofInt(window,
                "statusBarColor", window.getStatusBarColor(), toColor);
        statusBarColor.setEvaluator(ARGB_EVALUATOR);
        animList.add(statusBarColor);

        ObjectAnimator navigationBarColor = ObjectAnimator.ofInt(window,
                "navigationBarColor", window.getNavigationBarColor(), toColor);
        navigationBarColor.setEvaluator(ARGB_EVALUATOR);
        animList.add(navigationBarColor);

        ObjectAnimator backgroundColor = ObjectAnimator.ofObject(colorDrawable, "color", ARGB_EVALUATOR, toColor);
        animList.add(backgroundColor);

        final AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(1000L);
        animSet.playTogether(animList);
        animSet.start();

        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //mHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_NEXT_ACTIVITY, 500L);
                if (Nammu.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    mHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_NEXT_ACTIVITY, 500L);
                else
                    checkPermissionAndThenLoad();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkPermissionAndThenLoad() {
        //check for permission
        if (Nammu.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Log.d(TAG, "checkPermissionAndThenLoad has permission...");
            mHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_NEXT_ACTIVITY, 500L);
        } else {
            if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d(TAG, "checkPermissionAndThenLoad shouldShowRequestPermissionRationale...");
                new AlertDialog.Builder(this).setMessage(R.string.required_permissions_promo).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tryRequestPermission();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();
            } else {
                Log.d(TAG, "checkPermissionAndThenLoad askForPermission...");
                tryRequestPermission();
            }
        }
    }

    private void tryRequestPermission() {
        Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, this);
        mRequestTimeMillis = SystemClock.elapsedRealtime();
    }

    private void startSettingsPermission() {
        final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse(PACKAGE_URI_PREFIX + getPackageName()));
        startActivity(intent);
    }
    @Override
    public void permissionGranted() {
        mHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_NEXT_ACTIVITY, 200L);
    }

    @Override
    public void permissionRefused() {
        final long currentTimeMillis = SystemClock.elapsedRealtime();
        // If the permission request completes very quickly, it must be because the system
        // automatically denied. This can happen if the user had previously denied it
        // and checked the "Never ask again" check box.
        if ((currentTimeMillis - mRequestTimeMillis) < 250L) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.enable_permission_procedure).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startSettingsPermission();
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).create().show();

        } else {
            finish();
        }
    }
}
