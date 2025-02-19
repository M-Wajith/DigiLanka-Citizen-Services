package com.example.digilanka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.widget.ImageButton;

public class NicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nic);

        // Load the first fragment
        if (savedInstanceState == null) {
            loadFragment(new NicFirstFragment());
        }
        // Set up the back button to navigate back to DashboardActivity
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // This closes PassportActivity and returns to the previous activity (DashboardActivity)
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_nic, fragment)
                .commit();
    }
}
