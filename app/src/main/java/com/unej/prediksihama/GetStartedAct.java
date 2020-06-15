package com.unej.prediksihama;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GetStartedAct extends AppCompatActivity {

    Animation top_to_bottom, bottom_to_top, fade_in;
    View appBgGetStarted, logo;
    TextView slogan;
    Button lanjutkan, info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        // Load animation
        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
        bottom_to_top = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Load element
//        appBgGetStarted = findViewById(R.id.appbg_getstarted);
        logo = findViewById(R.id.logo);
        slogan = findViewById(R.id.slogan);
        lanjutkan = findViewById(R.id.btnLanjutkan);
        info = findViewById(R.id.btnInfo);

        // Run animation
        appBgGetStarted.startAnimation(fade_in);
        logo.startAnimation(top_to_bottom);
        slogan.startAnimation(top_to_bottom);
        lanjutkan.startAnimation(bottom_to_top);
        info.startAnimation(bottom_to_top);
    }

    public void btnLanjutkanOnClick(View view){
        Intent lanjutkan = new Intent(GetStartedAct.this, MapViewAct.class);
        startActivity(lanjutkan);
    }

    public void btnInfoOnClick(View view){
        Intent info = new Intent(GetStartedAct.this, InfoAct.class);
        startActivity(info);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
    }
}
