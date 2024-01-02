package com.example.defencecommander;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.logging.Level;

public class ScoresActivity extends AppCompatActivity {

    public static int ScoresValue;
    private MainActivity mainActivity;
    public static int id;
    public static String name;
    public static int level;
    public static int score;

    public static TextView scoreBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        setupFullScreen();

        mainActivity = MainActivity.getInstance();


        scoreBoard = findViewById(R.id.Scores);

        ScoresValue = getIntent().getIntExtra("SCORE",0);
        level =  getIntent().getIntExtra("LEVEL",0);
        name = getIntent().getStringExtra("NAME");


//        level = getIntent().getStringExtra("LEVEL");

        System.out.println("------ Level,Score : --------"+ ScoresValue + " : "+ level);


        ScoresDataBaseHandler dbh =
                new ScoresDataBaseHandler(this,null,name, level, ScoresValue);

        new Thread(dbh).start();

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

    public void setResults(String s) {
        scoreBoard.setText(s);
    }
    public void Exit(View v){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        System.exit(0);


    }
}