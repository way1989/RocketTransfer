package com.guo.duoduo.anyshareofandroid.ui.main;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.guo.duoduo.anyshareofandroid.R;

import java.util.ArrayList;
import java.util.List;


public class WelcomeActivity extends Activity {
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
                mHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_NEXT_ACTIVITY, 500L);
            }
        });
    }
}
