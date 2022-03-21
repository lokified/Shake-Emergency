package com.loki.credo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    TextView mainTitle, desTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mainTitle = findViewById(R.id.mainTitle);
        desTitle = findViewById(R.id.desTitle);

        Typeface mLight = Typeface.createFromAsset(getAssets(), "fonts/MLight.ttf");
        Typeface mRegular = Typeface.createFromAsset(getAssets(), "fonts/MRegular.ttf");

        mainTitle.setTypeface(mRegular);
        desTitle.setTypeface(mLight);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}