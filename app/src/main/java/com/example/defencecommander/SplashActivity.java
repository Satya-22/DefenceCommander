package com.example.defencecommander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 10000;
    private static final String TAG = "SplashActivity";

    public static int screenHeight;
    public static int screenWidth;

    public ConstraintLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SoundPlayer.getInstance().setupSound(this,"background",R.raw.background,true);
        layout = findViewById(R.id.splash_layout);

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        setupFullScreen();
        getScreenDimensions();
        runOnUiThread(() ->SoundPlayer.getInstance().start("background"));
        startSplash();

    }

    private void getScreenDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }
    private void setupFullScreen() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void startSplash(){



       // ImageView titleImage = findViewById(R.id.splash_title);
       // titleImage.setVisibility(View.VISIBLE);
//        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
//        fadeIn.setDuration(1000);
//        fadeIn.setInterpolator(new DecelerateInterpolator());
//        fadeIn.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                SoundPlayer.getInstance().start("interceptor_blast");
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//                AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
//                fadeOut.setDuration(1000);
//                fadeOut.setInterpolator(new AccelerateInterpolator());
//                fadeOut.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                        SoundPlayer.getInstance().start("interceptor_blast");
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        new Handler().postDelayed(() -> {
//                            // This method will be executed once the timer is over
//                            // Start your app main activity
//                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
//                            startActivity(i);
//                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out); // new act, old act
//                            // close this activity
//                            finish();
//                        }, SPLASH_TIME_OUT);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//                titleImage.startAnimation(fadeOut);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        titleImage.startAnimation(fadeIn);
       //
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (screenWidth*0.8), (int) (screenHeight*0.40));

// Set the LayoutParams on the ImageView
//        imageView2.setLayoutParams(layoutParams);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.title);

        imageView.setX((float) (screenWidth*0.10));
        imageView.setY((float) ((screenHeight/2)-100));
        imageView.setZ(2);

        imageView.setLayoutParams(layoutParams);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"alpha",0,1);
        objectAnimator.setDuration(SPLASH_TIME_OUT);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
               // layout.removeView(imageView);

                runOnUiThread(() ->{
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                });

            }

            @Override
            public void onAnimationStart(Animator animation) {
                SoundPlayer.getInstance().start("background");
            }
        });
        layout.addView(imageView);
        objectAnimator.start();
    }
}