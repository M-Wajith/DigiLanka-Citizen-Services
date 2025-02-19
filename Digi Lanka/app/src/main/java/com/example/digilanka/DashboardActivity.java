package com.example.digilanka;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DashboardActivity extends AppCompatActivity {

    private String nicNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        nicNumber = getIntent().getStringExtra("NIC_NUMBER");
        Log.d("DashboardActivity", "Received NIC Number: " + nicNumber);

        Toast.makeText(this, "NIC Number: " + nicNumber, Toast.LENGTH_LONG).show();
        TextView nicTextView = findViewById(R.id.nicTextView);
        nicTextView.setText("NIC Number: " + nicNumber);

        fetchCitizenInfo(nicNumber);

        Button passportBtn = findViewById(R.id.passportBtn);
        Button nicBtn = findViewById(R.id.nicBtn);
        Button mediBtn = findViewById(R.id.mediBtn);
        Button writtenBtn = findViewById(R.id.writtenBtn);
        Button trialBtn = findViewById(R.id.trialBtn);

        passportBtn.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, PassportActivity.class)));
        nicBtn.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, NicActivity.class)));
        mediBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MedicalActivity.class);
            intent.putExtra("NIC_NUMBER", nicNumber); // Pass the NIC number
            startActivity(intent);
        });
        writtenBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, WrittenActivity.class);
            intent.putExtra("NIC_NUMBER", nicNumber); // Pass the NIC number
            startActivity(intent);
        });
        trialBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PracticalActivity.class);
            intent.putExtra("NIC_NUMBER", nicNumber);
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> handleNavigation(item));

        // Set the default selected item to "Home"
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private boolean handleNavigation(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            // Stay on DashboardActivity (Home screen)
            return true;
        } else if (itemId == R.id.nav_status) {
            // Navigate to StatusActivity
            Intent intent = new Intent(DashboardActivity.this, StatusActivity.class);
            intent.putExtra("NIC_NUMBER", nicNumber);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId == R.id.nav_logout) {
            showLogoutConfirmationDialog();
            return true;
        } else {
            return false;
        }
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    // Redirect to MainActivity
                    Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Close the current activity
                })
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss())
                .create()
                .show();
    }

    private void fetchCitizenInfo(String nicNumber) {
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://10.0.2.2:5000/api/citizen/" + nicNumber);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        return new JSONObject(response.toString());
                    }
                } catch (Exception e) {
                    Log.e("FetchCitizenInfo", "Error fetching citizen info", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                if (result != null) {
                    try {
                        String firstName = result.getString("first_name");
                        String lastName = result.getString("last_name");
                        String dob = result.getString("date_of_birth");

                        TextView nameTextView = findViewById(R.id.nameTextView);
                        nameTextView.setText("Name: " + firstName + " " + lastName);

                        TextView dobTextView = findViewById(R.id.dobTextView);
                        dobTextView.setText("Date of Birth: " + dob);
                    } catch (Exception e) {
                        Log.e("FetchCitizenInfo", "Error parsing JSON", e);
                    }
                } else {
                    Toast.makeText(DashboardActivity.this, "Citizen not found", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
