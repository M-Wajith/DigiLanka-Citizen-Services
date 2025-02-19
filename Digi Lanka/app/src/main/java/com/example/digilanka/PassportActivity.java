package com.example.digilanka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PassportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FirstFragment())
                    .commit();
        }

        // Set up the back button to navigate back to DashboardActivity
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // This closes PassportActivity and returns to the previous activity (DashboardActivity)
        });
    }

    public void navigateToSecondFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new SecondFragment())
                .addToBackStack(null)
                .commit();
    }

    public void navigateToThirdFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ThirdFragment())
                .addToBackStack(null)
                .commit();
    }

    public void navigateFirstFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new FirstFragment())
                .addToBackStack(null) // Optional: to allow going back to SecondFragment
                .commit();
    }
}
