package com.yo4gis.consumersurvey.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.yo4gis.consumersurvey.R;
import com.yo4gis.consumersurvey.databinding.ActivitySplashBinding;
import com.yo4gis.consumersurvey.utilities.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    Animation imageAnimation, bounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(com.yo4gis.consumersurvey.R.layout.activity_splash);*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initializeView();
    }

    private void initializeView(){
        imageAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_image_transition);
        bounce = AnimationUtils.loadAnimation(this, R.anim.bounse);
        binding.imgBackground.setAnimation(imageAnimation);
        binding.pin.setAnimation(bounce);
        startSeeding();
    }

    public void startSeeding() {
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 5000) {
                        sleep(100);
                        waited += 100;
                    }
                    openMainActivity();

                } catch (InterruptedException e) {

                } finally {
                    openMainActivity();
                }

            }
        };
        splashTread.start();

    }

    public void openMainActivity() {


            Intent intent;
            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }