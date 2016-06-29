package com.codepath.simpletodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * @author Zhisheng Zhou
 */

public class LauchingActivity extends AppCompatActivity implements Animation.AnimationListener {

    private static final String LOGTAG = "SplashScreenActivity";
    private long _splashTime = 2000;
    public static Context contextOfApplication;
    private ImageView launchImage;
    // Animation
    private Animation animFadeIn, animFadeOut;
    private SharedPreferences sharedPreferences_put;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lauching);

        contextOfApplication = getApplicationContext();
        launchImage = (ImageView) findViewById(R.id.launch_image);
        // load the animation
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        // set animation listener
        animFadeIn.setAnimationListener(this);

        launchImage.setVisibility(View.VISIBLE);
        launchImage.startAnimation(animFadeIn);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                sharedPreferences_put = getSharedPreferences(Constants.User_Setting_file, Activity.MODE_PRIVATE);
                Boolean isFirstLaunch = sharedPreferences_put.getBoolean(Constants.IS_FIRST_LAUNCH, false);
                Intent main_Intent = null;
                if (isFirstLaunch)
                    createDB();
                main_Intent = new Intent(LauchingActivity.this, MainActivity.class);
                startActivity(main_Intent);
                // animating switching of 2 activities with fadein fadeout animation
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            private void createDB() {
                DatabaseHelper dbHelper = new DatabaseHelper(
                        LauchingActivity.this);
                dbHelper.close();
            }
        }, _splashTime);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // Take any action after completing the animation
        // check for fade in animation
        if (animation == animFadeIn) {
            launchImage.startAnimation(animFadeOut);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
}
