package com.example.defencecommander;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<Integer> topScores = new ArrayList<>();

    static ConstraintLayout layout;
    public static int screenHeight;
    public static ImageView launcher1,launcher2,launcher3;
    public static ImageView gameOver;

    private static int levelNumber;

    private static MainActivity context;
    public static int scoreValue;
    public static int screenWidth;

//    public static

   public ScoresDataBaseHandler dbh;

    static MissileMaker missileMaker;

    static boolean appExit = false;
    public static TextView levelText, scoreText;

    private ActivityResultLauncher<Intent> scoresActivityResultLauncher;

    public ScoresDataBaseHandler scoresDataBaseHandler;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         context = this;
        setupFullScreen();
        getScreenDimensions();

        SoundPlayer.getInstance().setupSound(this, "interceptor_blast", R.raw.interceptor_blast,false);
        SoundPlayer.getInstance().setupSound(this, "launch_interceptor", R.raw.launch_interceptor,false);
        SoundPlayer.getInstance().setupSound(this, "interceptor_hit_plane", R.raw.interceptor_hit_missile,false);
        SoundPlayer.getInstance().setupSound(this,"missile_miss",R.raw.missile_miss,false);
        SoundPlayer.getInstance().setupSound(this,"launch_missile",R.raw.launch_missile,false);
        SoundPlayer.getInstance().setupSound(this,"base_blast",R.raw.base_blast,false);


        layout = findViewById(R.id.main_layout);
        levelText = findViewById(R.id.Level);
        scoreText = findViewById(R.id.Score);
        launcher1 = findViewById(R.id.Base1);
        launcher2 = findViewById(R.id.Base2);
        launcher3 = findViewById(R.id.Base3);
//        gameOver = findViewById(R.id.GameOver);

        new ScrollingBackground(this, layout, R.drawable.clouds, 60000);

        layout.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                handleTouch(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        });

        missileMaker= new MissileMaker(this, screenWidth, screenHeight);

         new Thread(missileMaker).start();

        scoresActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleDbResult);

    }

    public ConstraintLayout getLayout() {
        return layout;
    }

    public static MainActivity getInstance(){
        return context;
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

    public void handleDbResult(ActivityResult result) {
        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }

        if (result.getResultCode() == RESULT_OK) {
            appExit = result.getData().getBooleanExtra("EXIT",false);
            if(appExit) {
                EndGame();
            }
        }
    }

    public void EndGame(){
        finish();
    }
    public void setLevel(final int i) {
        levelNumber =i;
        runOnUiThread(() -> levelText.setText(String.format(Locale.getDefault(), "Level:%d", i)));
    }

    public void incrementScore() {
        scoreValue++;
        scoreText.setText(String.format(Locale.getDefault(), "%d", scoreValue));
    }


    public void removePlane(Missile m) {
        missileMaker.removePlane(m);
    }
    private void getScreenDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }

    @SuppressLint("SuspiciousIndentation")
    public void handleTouch(float x1, float y1) {

        HashMap<Pair<Double,Double>,Double> hashMap = new HashMap<>();

        if(launcher1 != null) {
            double startX1 = launcher1.getX() + (0.5 * launcher1.getWidth());
            double startY1 = launcher1.getY() + (0.5 * launcher1.getHeight());

            double distance = calculateDistance(startX1, startY1, x1, y1);

            hashMap.put(new Pair(startX1,startY1),distance);
        }
       if(launcher2 !=null) {
           double startX2 = launcher2.getX() + (0.5 * launcher2.getWidth());
           double startY2 = launcher2.getY() + (0.5 * launcher2.getHeight());

           double distance1 = calculateDistance(startX2, startY2, x1, y1);
           hashMap.put(new Pair(startX2,startY2),distance1);
       }
       if(launcher3 != null) {
           double startX3 = launcher3.getX() + (0.5 * launcher3.getWidth());
           double startY3 = launcher3.getY() + (0.5 * launcher3.getHeight());

           double distance2 = calculateDistance(startX3, startY3, x1, y1);
           hashMap.put(new Pair(startX3,startY3),distance2);
       }

       if(launcher1 != null || launcher2 != null || launcher3 != null) {

           double minDistance = Collections.min(hashMap.values());
           Pair p = null;
           for (Map.Entry<Pair<Double, Double>, Double> entry : hashMap.entrySet()) {
               if (entry.getValue() == minDistance) {
                   p = entry.getKey();
                   break;
               }
           }

           double startX;
           double startY;

           startX = (double) p.first;
           startY = (double) p.second;

           float a = calculateAngle(startX, startY, x1, y1);
           Log.d(TAG, "handleTouch: " + a);
           Interceptor i = new Interceptor(this, (float) (startX - 10), (float) (startY - 30), x1, y1);
           SoundPlayer.getInstance().start("launch_interceptor");
           i.launch();
       }
       else{
           //gameOver.setVisibility(View.VISIBLE);
           this.runOnUiThread(() -> {
               ImageView imgv = new ImageView(this);
               imgv.setImageResource(R.drawable.game_over);
               imgv.setX(screenHeight / 2);
               imgv.setY(screenWidth / 2);
               imgv.setZ(2);
               ObjectAnimator gameOver = ObjectAnimator.ofFloat(imgv, "alpha", 0, 1);
               gameOver.setDuration(3000);
               gameOver.start();
           });
       }

    }

    public void openScoreCard(){
        scoresDataBaseHandler = new ScoresDataBaseHandler(null,this,"yoga",levelNumber,scoreValue);
        new Thread(scoresDataBaseHandler).start();

    }
    public void stopApp(){
        finish();
    }

    public void gameOver() {

        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.game_over);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (screenWidth*0.8), (int) (screenHeight*0.40));

// Set the LayoutParams on the ImageView
//        imageView2.setLayoutParams(layoutParams);
        imageView2.setZ(2);
        imageView2.setX((float) (screenWidth*0.10));
        imageView2.setY((float) ((screenHeight/2)-100));




        HandlerThread handlerThread = new HandlerThread("MyThread");
        handlerThread.start();

        Handler handler = new Handler(handlerThread.getLooper());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator yAnim1 = ObjectAnimator.ofFloat(imageView2, "alpha", 0, 1);
                yAnim1.setDuration(3000);

                yAnim1.start();
                imageView2.setLayoutParams(layoutParams);
                layout.addView(imageView2);

                yAnim1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

//                        dbh = new ScoresDataBaseHandler();
//
//
//                        new Thread(dbh).start();

                        openScoreCard();

                    }
                });
            }
        });
    }

    public void openScoresPage(String name){
        Intent i = new Intent(context, ScoresActivity.class);
        i.putExtra("NAME",name);
        i.putExtra("SCORE",scoreValue);
        i.putExtra("LEVEL",levelNumber);
        startActivity(i);

    }
    public double calculateDistance(double x1,double y1,double x2,double y2){

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public float calculateAngle(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil(-angle / 360) * 360;
        return (float) (190.0f - angle);
    }
    public void applyInterceptorBlast(Interceptor interceptor, int id) {
        missileMaker.applyInterceptorBlast(interceptor, id);
    }

    public void getScores(ArrayList<Integer> ar) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Boolean go= true;
//                String name=null;

                topScores.clear();
                topScores.addAll(ar);
                for (Integer i : topScores) {
                    go = true;
                    System.out.println("scores:" + i);
                    if (scoreValue > i) {
                        go = false;
                        final EditText input = new EditText(context);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked Yes button
                                String name = input.getText().toString();
                                openScoresPage(name);


                            }
                        });



                        builder.setNegativeButton("NO", (dialog, which) -> {
                           // dialog.dismiss();
                            MainActivity.super.onBackPressed();
                        });

                        builder.setTitle(" You are a Top-Player!");
                        builder.setMessage("Please enter your initials (upto 3 characters):");


                        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

                        InputFilter[] filters = new InputFilter[1];
                        filters[0] = new InputFilter.LengthFilter(3);
                        input.setFilters(filters);

//                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
                if(go) {
                    openScoresPage(null);
                }

            }
        });

    }
}