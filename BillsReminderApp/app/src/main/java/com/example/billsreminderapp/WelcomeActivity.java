package com.example.billsreminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends AppCompatActivity {
    // This is the loading time of the splash screen
    private static int SPLASH_TIME_OUT = 7000; // 7 sec

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // This method will be executed once the timer is over start application in main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent startIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(startIntent);
                // close this activity
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
