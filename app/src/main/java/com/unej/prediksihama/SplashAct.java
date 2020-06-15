package com.unej.prediksihama;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashAct extends AppCompatActivity {

    ImageView logo;
    Animation zoom_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        zoom_in = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        logo = findViewById(R.id.logo);
        logo.startAnimation(zoom_in);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent next = new Intent(SplashAct.this, GetStartedAct.class);
                startActivity(next);
                finish();
            }
        },2000);
    }
}
