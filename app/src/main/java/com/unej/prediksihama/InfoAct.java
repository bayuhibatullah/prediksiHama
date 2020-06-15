package com.unej.prediksihama;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoAct extends AppCompatActivity {

    Animation zoomIn, bottomToTop;
    ImageView ilustrasi;
    TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Load Animation
        bottomToTop = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        // Load Element
        ilustrasi = findViewById(R.id.ilustrasi);
        textInfo = findViewById(R.id.textInfo);

        // Run Animation
        ilustrasi.startAnimation(zoomIn);
        textInfo.startAnimation(bottomToTop);
    }

    public void btnBackiOnClick(View view){
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
    }
}
