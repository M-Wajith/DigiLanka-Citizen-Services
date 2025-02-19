package com.example.digilanka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class StatusActivity extends AppCompatActivity {

    private View mainContent; // To hold the main content of the activity
    private String nicNumber; // Variable to store the NIC number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // Reference to main content layout
        mainContent = findViewById(R.id.main_content);

        // Retrieve NIC number from Intent
        nicNumber = getIntent().getStringExtra("NIC_NUMBER");

        // Set up the back button to navigate back to DashboardActivity
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(StatusActivity.this, DashboardActivity.class);
            intent.putExtra("NIC_NUMBER", nicNumber); // Pass the NIC number back if needed
            startActivity(intent);
            finish(); // This closes StatusActivity
        });

        // Set up button listeners
        findViewById(R.id.passportStatusBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show Passport Status Fragment and pass nicNumber
                PassportStatusFragment passportStatusFragment = PassportStatusFragment.newInstance(nicNumber);
                navigateToFragment(passportStatusFragment);
            }
        });

        findViewById(R.id.nicStatutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show NIC Status Fragment only
                NicStatusFragment nicStatusFragment = NicStatusFragment.newInstance(nicNumber);
                navigateToFragment(nicStatusFragment);
            }
        });

        findViewById(R.id.licenseStatutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show License Status Fragment and pass nicNumber
                LicenseStatusFragment licenseStatusFragment = LicenseStatusFragment.newInstance(nicNumber);
                navigateToFragment(licenseStatusFragment);
            }
        });
    }

    private void navigateToFragment(Fragment fragment) {
        // Hide main content
        mainContent.setVisibility(View.GONE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.status_fragment_container, fragment);
        transaction.addToBackStack(null); // Allows user to navigate back
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        // If there's a fragment in the container, handle back press to show main content
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            mainContent.setVisibility(View.VISIBLE); // Show main content again
        } else {
            super.onBackPressed();
        }
    }
}
