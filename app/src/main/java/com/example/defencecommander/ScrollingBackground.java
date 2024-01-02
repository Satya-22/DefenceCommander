package com.example.defencecommander;

import static com.example.defencecommander.MainActivity.screenHeight;
import static com.example.defencecommander.MainActivity.screenWidth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public class ScrollingBackground{
    private final Context context;
    private final ViewGroup layout;
    private ImageView backImageA;
    private ImageView backImageB;
    public int rotation_value =0;
    private final long duration;
    private final int resId;
    private static final String TAG = "ScrollingBackground";
    private static boolean running = true;

    ScrollingBackground(Context context, ViewGroup layout, int resId, long duration) {
        this.context = context;
        this.layout = layout;
        this.resId = resId;
        this.duration = duration;

        setupBackground();
    }


    private void setupBackground() {
        backImageA = new ImageView(context);
        backImageB = new ImageView(context);

        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(screenWidth , screenHeight);
        backImageA.setLayoutParams(params);
        backImageB.setLayoutParams(params);

        layout.addView(backImageA);
        layout.addView(backImageB);

        Bitmap backBitmapA = BitmapFactory.decodeResource(context.getResources(), resId);
        Bitmap backBitmapB = BitmapFactory.decodeResource(context.getResources(), resId);

        backImageA.setImageBitmap(backBitmapA);
        backImageB.setImageBitmap(backBitmapB);

        backImageA.setScaleType(ImageView.ScaleType.FIT_XY);
        backImageB.setScaleType(ImageView.ScaleType.FIT_XY);

        backImageA.setZ(2);
        backImageB.setZ(2);

        animateBack();
        //new Thread(this).start();
    }

    private void animateBack() {

        ValueAnimator animator = ValueAnimator.ofFloat(0.25f, 0.95f);
//          ValueAnimator animator = ValueAnimator.ofFloat(backImageA, "alpha", 1, 0);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(duration);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(@NonNull Animator animation, boolean isReverse) {
               SoundPlayer.getInstance().start("background");
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation, boolean isReverse) {
               SoundPlayer.getInstance().stop("background");
            }
        });
        animator.addUpdateListener(animation -> {
            if (!running) {
                animator.cancel();
                return;
            }
            final float progress = (float) animation.getAnimatedValue();
            float width = screenWidth + getBarHeight();

            float a_translationX = width * progress;
            float b_translationX = width * progress - width;

            backImageA.setTranslationX(a_translationX);
            backImageB.setTranslationX(b_translationX);

        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                float animatedValue = (float) valueAnimator.getAnimatedValue();
//                System.out.println("alpha :"+backImageA.getAlpha());
//                float p = animatedValue - prev[0];
//                System.out.println("animated: "+animatedValue);


                if(rotation_value%2==0){
                    backImageA.setAlpha(animatedValue);
                    System.out.println("1111: "+ animatedValue);

                    backImageB.setAlpha(animatedValue);

                } else if (rotation_value%2==1)
                {
                    backImageA.setAlpha(1.00F-animatedValue+20.00F);
                    System.out.println("2222: "+ (1.00F-animatedValue+0.20F));

                    backImageB.setAlpha(1.00F-animatedValue+20.00F);

                }
                if(animatedValue==0.95F){
                    System.out.println("IN");
                    rotation_value++;}
//                prev[0] = animatedValue;




            }
        });
        animator.start();
    }


    private int getBarHeight() {
        return (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);
    }

}
