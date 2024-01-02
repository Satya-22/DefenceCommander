package com.example.defencecommander;

import android.util.Log;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ScoresDataBaseHandler implements Runnable {

    private final String TAG = getClass().getSimpleName();
    private final ScoresActivity context;
    private final SimpleDateFormat sdf =
            new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
    private Connection conn;
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String SCORES_TABLE = "AppScores";
    private final static String dbName = "chri5558_missile_defense";
    private final static String dbURL = "jdbc:mysql://christopherhield.com:3306/" + dbName;
    private final static String dbUser = "chri5558_student";
    private final static String dbPass = "ABC.123";

    private boolean top10flag = false;

   // private final int id;
    private String name;
    private int  score;

    private int level;

    private MainActivity mainActivity;

    ScoresDataBaseHandler(ScoresActivity ctx, MainActivity mainActivity,String name, int score1, int level1) {
        context = ctx;
        this.name = name;
        this.score = score1;
        this.level = level1;
        this.mainActivity = mainActivity;
    }

    public void run() {

            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

                StringBuilder sb = new StringBuilder();

                if(context!=null) {

                    Statement stmt = conn.createStatement();

                    if(name!=null) {

                        String sql = "insert into " + SCORES_TABLE + " values (" +
                                System.currentTimeMillis() + ", '" + name + "', " + level + ", " + score + ")";
//                        String sql = "insert into " + SCORES_TABLE + " values (" +
//                                System.currentTimeMillis() + ", '" + name + "', " + 1 + ", " + 102 + ")";
                        Log.d(TAG, "run: " + sql);

                        int result = stmt.executeUpdate(sql);

                        System.out.println(result);


                        stmt.close();
                    }

                    // String response = "Date/Time " + " Init " +" Level "+" Score "+"\n";

                    // sb.append(response);
//            sb.append(String.format(Locale.getDefault(),
//                    " %12s   %-12s   %-12s  %-12s%n", "Date/Time","Init", "Level","Score"));

                    sb.append(String.format(Locale.getDefault(),
                            "%12s   %-12s    %-12s    %-12s   %12s%n", "#","Init", "Level", "Score", "Date/Time"));
                    sb.append(getAllTopTen());

                    context.setResults(sb.toString());
                    conn.close();
                }
                else{
                    ArrayList<Integer> ar = getAllTopTenScores();
                    mainActivity.getScores(ar);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private String getAllTopTen() throws SQLException {
        Statement stmt = conn.createStatement();

        String sql = "select * from " + SCORES_TABLE + " order by Score desc limit 10";

        StringBuilder sb = new StringBuilder();

        ResultSet rs = stmt.executeQuery(sql);
        int i =1;
        while (rs.next()) {
           // int id = rs.getInt(1);
            String name = rs.getString(2);
            int level= rs.getInt(4);
            int score = rs.getInt(3);
            long millis = rs.getLong(1);

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm");
            String formattedDate = dateFormat.format(new Date(millis));

            sb.append(String.format(Locale.getDefault(),
                    "%12s   %-12s    %-12d   %-12d    %12s%n", i++,name.trim(), level,score,formattedDate.trim()));
            System.out.println("ID : "+ formattedDate+ ", '" + name + "', " + score + ", " + level + ", ");
        }
        rs.close();
        stmt.close();

        return sb.toString();
    }

    private ArrayList<Integer> getAllTopTenScores() throws SQLException {
        Statement stmt = conn.createStatement();

        String sql = "select score from " + SCORES_TABLE + " order by Score desc limit 10";

        StringBuilder sb = new StringBuilder();

        ResultSet rs = stmt.executeQuery(sql);
        int i=1;

        System.out.println("dsdssd:  "+rs.next());
        ArrayList<Integer> scoresArray= new ArrayList<>();
        while (rs.next()) {
            // int id = rs.getInt(1);
            int score = rs.getInt("score");
            scoresArray.add(Integer.valueOf(score));




        }
        System.out.println("khkbhk: "+scoresArray);
        rs.close();
        stmt.close();

        return scoresArray;
    }



}