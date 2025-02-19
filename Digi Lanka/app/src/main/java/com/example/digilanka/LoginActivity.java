package com.example.digilanka;

import android.content.Intent; // Import Intent
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText nicEditText;
    private Button continueButton;
    private TextView copyrightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nicEditText = findViewById(R.id.nicEditText);
        continueButton = findViewById(R.id.button);
        copyrightTextView = findViewById(R.id.copyrightTextView);

        continueButton.setOnClickListener(v -> {
            String nicNumber = nicEditText.getText().toString().trim();
            if (!nicNumber.isEmpty()) {
                NetworkUtils.checkNIC(nicNumber, new NetworkUtils.NICCallback() {
                    @Override
                    public void onSuccess(String response) {
                        // Assuming the response will be a JSON object
                        // You can parse it if necessary.
                        // For now, let's assume it returns a success message.
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "NIC validated!", Toast.LENGTH_SHORT).show();
                            // Navigate to CameraActivity after validation
                            Intent intent = new Intent(LoginActivity.this, CameraActivity.class);
                            intent.putExtra("NIC_NUMBER", nicNumber);
                            startActivity(intent);
                            finish(); // Optional: Close LoginActivity if you don't want to return
                        });
                    }

                    @Override
                    public void onError(String error) {
                        // Handle error response
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "Please enter NIC number", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
