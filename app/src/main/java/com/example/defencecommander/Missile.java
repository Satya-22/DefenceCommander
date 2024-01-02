package com.example.defencecommander;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.RectF;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class Missile {

    private final MainActivity mainActivity;
    private final ImageView imageView;
    private final AnimatorSet aSet = new AnimatorSet();
    private final int screenHeight;
    private final int screenWidth;
    private final long screenTime;
    private static final String TAG = "Missile";
    private static boolean Base1 = true;
    private static boolean Base2 = true;
    private static boolean Base3 = true;
    public static boolean hit = false;

    Missile(int screenWidth, int screenHeight, long screenTime, final MainActivity mainActivity) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.screenTime = screenTime;
        this.mainActivity = mainActivity;


        imageView = new ImageView(mainActivity);
        imageView.setRotation(180);

        mainActivity.runOnUiThread(() -> mainActivity.getLayout().addView(imageView));

    }

    AnimatorSet setData(final int drawId) {
        mainActivity.runOnUiThread(() -> imageView.setImageResource(drawId));

        int startX = (int) (Math.random() * screenWidth * 0.8);
        int endX = (startX + (Math.random() < 0.5 ? 150 : -150));
        final ImageView iv1 = new ImageView(mainActivity);
        iv1.setImageResource(R.drawable.i_explode);

        final ImageView iv2 = new ImageView(mainActivity);
        iv2.setImageResource(R.drawable.blast);

        ObjectAnimator yAnim = ObjectAnimator.ofFloat(imageView,"y",-200, (screenHeight + 200));
        yAnim.setInterpolator(new LinearInterpolator());
        yAnim.setDuration(screenTime);
        yAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.runOnUiThread(() -> {
                    if (!hit) {
                        mainActivity.getLayout().removeView(imageView);
                        mainActivity.removePlane(Missile.this);
                        iv1.setX(endX);
                        iv1.setY(screenHeight - 30);
                        iv1.setZ(2);


                        ObjectAnimator obj = ObjectAnimator.ofFloat(iv1, "alpha", 1, 0);
                        //obj.setInterpolator(new LinearInterpolator());
                        obj.setDuration(3000);
                        obj.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                // mainActivity.runOnUiThread(() -> {
                                System.out.println("yoga ");
                                mainActivity.getLayout().removeView(iv1);

                                // });
                            }
                        });
                        obj.start();
                        SoundPlayer.getInstance().start("missile_miss");
                        mainActivity.getLayout().addView(iv1);
                    }
                    Log.d(TAG, "run: NUM VIEWS " +
                            mainActivity.getLayout().getChildCount());
                });

            }
        });
        yAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            boolean soundPlayedBase1,soundPlayedBase2,soundPlayedBase3 = false;
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                float view1X = imageView.getX();
                float view1Y = imageView.getY();

                // Create RectF objects for the ImageView and the other view

                // Check if the RectF objects intersect
                if (Base1){
                    RectF view1Rect = new RectF(view1X, view1Y, view1X + imageView.getWidth(), view1Y + imageView.getHeight());
                    RectF view2Rect = new RectF(MainActivity.launcher1.getX(), MainActivity.launcher1.getY(), MainActivity.launcher1.getX() + MainActivity.launcher1.getWidth(), MainActivity.launcher1.getY() + MainActivity.launcher1.getHeight());

                    if (view1Rect.intersect(view2Rect)) {


                        iv2.setX(endX);
                        iv2.setY(screenHeight - 30);
                        iv2.setZ(2);
                        System.out.println("Base1 Hit !!!!!!!!");

                        mainActivity.getLayout().removeView(MainActivity.launcher1);
                        ObjectAnimator obj2 = ObjectAnimator.ofFloat(iv2, "alpha", 1, 0);
                        //obj.setInterpolator(new LinearInterpolator());
                        obj2.setDuration(3000);
                        obj2.addListener(new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationStart(Animator animation) {
                                if(!soundPlayedBase1) {
                                    SoundPlayer.getInstance().start("base_blast");
                                    soundPlayedBase1 = true;
                                }
                            }

                        });
                        obj2.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                // mainActivity.runOnUiThread(() -> {
                                mainActivity.getLayout().removeView(iv2);
                                //SoundPlayer.getInstance().start("base_blast");
                                Base1 = false;
                                MainActivity.launcher1 = null;
                            }
                        });
                        obj2.start();

                        ViewGroup currentParent = (ViewGroup) iv2.getParent(); // get the current parent of textView
                        if (currentParent != null) {
                            currentParent.removeView(iv2); // remove textView from its current parent
                        }
                        // constraintLayout.addView(textView);
                        mainActivity.getLayout().addView(iv2);
                    }
            }
                if (Base2){
                    RectF view1Rect = new RectF(view1X, view1Y, view1X + imageView.getWidth(), view1Y + imageView.getHeight());
                    RectF view2Rect = new RectF(MainActivity.launcher2.getX(), MainActivity.launcher2.getY(), MainActivity.launcher2.getX() + MainActivity.launcher2.getWidth(), MainActivity.launcher2.getY() + MainActivity.launcher2.getHeight());
                    if (view1Rect.intersect(view2Rect)) {
                        iv2.setX(endX);
                        iv2.setY(screenHeight - 30);
                        iv2.setZ(2);
                        System.out.println("Base2 Hit !!!!!!!!");
                        //SoundPlayer.getInstance().start("base_blast");
                        mainActivity.getLayout().removeView(MainActivity.launcher2);
                        ObjectAnimator obj2 = ObjectAnimator.ofFloat(iv2, "alpha", 1, 0);
                        //obj.setInterpolator(new LinearInterpolator());
                        obj2.setDuration(3000);
                        obj2.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                if(!soundPlayedBase2) {
                                    SoundPlayer.getInstance().start("base_blast");
                                    soundPlayedBase2 = true;
                                }
                            }
                        });
                        obj2.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                // mainActivity.runOnUiThread(() -> {

                                mainActivity.getLayout().removeView(iv2);

                                Base2 = false;
                                MainActivity.launcher2 = null;
                            }
                        });
                        obj2.start();

                        ViewGroup currentParent = (ViewGroup) iv2.getParent(); // get the current parent of textView
                        if (currentParent != null) {
                            currentParent.removeView(iv2); // remove textView from its current parent
                        }
                        // constraintLayout.addView(textView);
                        mainActivity.getLayout().addView(iv2);
                    }
                }
                if (Base3){
                    RectF view1Rect = new RectF(view1X, view1Y, view1X + imageView.getWidth(), view1Y + imageView.getHeight());
                    RectF view2Rect = new RectF(MainActivity.launcher3.getX(), MainActivity.launcher3.getY(), MainActivity.launcher3.getX() + MainActivity.launcher3.getWidth(), MainActivity.launcher3.getY() + MainActivity.launcher3.getHeight());
                    if (view1Rect.intersect(view2Rect)) {
                        iv2.setX(endX);
                        iv2.setY(screenHeight - 30);
                        iv2.setZ(2);
                        System.out.println("Base Hit3 !!!!!!!!");
                        //SoundPlayer.getInstance().start("base_blast");
                        mainActivity.getLayout().removeView(MainActivity.launcher3);
                        ObjectAnimator obj2 = ObjectAnimator.ofFloat(iv2, "alpha", 1, 0);
                        //obj.setInterpolator(new LinearInterpolator());
                        obj2.setDuration(3000);
                        obj2.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                if(!soundPlayedBase3) {
                                    SoundPlayer.getInstance().start("base_blast");
                                    soundPlayedBase3 = true;
                                }
                            }
                        });
                        obj2.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                // mainActivity.runOnUiThread(() -> {
                                mainActivity.getLayout().removeView(iv2);
                               // SoundPlayer.getInstance().start("base_blast");
                                Base3 = false;
                                MainActivity.launcher3 = null;
                            }
                        });
                        obj2.start();

                        ViewGroup currentParent = (ViewGroup) iv2.getParent(); // get the current parent of textView
                        if (currentParent != null) {
                            currentParent.removeView(iv2); // remove textView from its current parent
                        }
                        // constraintLayout.addView(textView);
                        mainActivity.getLayout().addView(iv2);
                    }
                }

            }
        });

        ObjectAnimator xAnim = ObjectAnimator.ofFloat(imageView, "x", startX, endX);
        xAnim.setInterpolator(new LinearInterpolator());
        xAnim.setDuration(screenTime);
        aSet.playTogether(xAnim, yAnim);

        return aSet;

    }
    void stop() {
        aSet.cancel();
    }

    float getX() {
        return imageView.getX();
    }

    float getY() {
        return imageView.getY();
    }

    float getWidth() {
        return imageView.getWidth();
    }

    float getHeight() {
        return imageView.getHeight();
    }

    void interceptorBlast(float x, float y) {

        hit = true;

        final ImageView iv = new ImageView(mainActivity);
        iv.setImageResource(R.drawable.explode);

        iv.setTransitionName("Missile Intercepted Blast");

        int w = imageView.getDrawable().getIntrinsicWidth();
        int offset = (int) (w * 0.5);

        iv.setX(x - offset);
        iv.setY(y - offset);
        iv.setRotation((float) (360.0 * Math.random()));

        aSet.cancel();
        mainActivity.getLayout().removeView(imageView);
        mainActivity.getLayout().addView(iv);

        final ObjectAnimator alpha = ObjectAnimator.ofFloat(iv, "alpha", 0.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.getLayout().removeView(imageView);
            }
        });
        alpha.start();
        hit = false;
    }
}
