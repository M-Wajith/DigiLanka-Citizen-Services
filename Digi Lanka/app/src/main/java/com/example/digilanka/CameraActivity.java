package com.example.digilanka;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button cameraButton;
    private ImageView capturedImageView;
    private String nicNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        nicNumber = getIntent().getStringExtra("NIC_NUMBER");
        Log.d("CameraActivity", "Received NIC Number: " + nicNumber);
        Toast.makeText(this, "NIC Number: " + nicNumber, Toast.LENGTH_LONG).show();

        cameraButton = findViewById(R.id.button);
        capturedImageView = findViewById(R.id.click_image);

        cameraButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            capturedImageView.setImageBitmap(imageBitmap);
            capturedImageView.setScaleX(-1f); // Flip the ImageView


            String capturedImageBase64 = convertBitmapToBase64(imageBitmap);
            Log.d("CameraActivity", "Base64 Image Length: " + capturedImageBase64.length());
            sendImageToServer(capturedImageBase64);
        }
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void sendImageToServer(String base64Image) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://10.0.2.2:5000/check_face");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("captured_face_image", base64Image);
                jsonParam.put("nic_number", nicNumber);  // Add NIC number to the payload
                Log.d("CameraActivity", "Sending payload: " + jsonParam.toString());

                // Write to output stream
                try (OutputStream os = conn.getOutputStream();
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
                    writer.write(jsonParam.toString());
                    writer.flush();
                }

                // Get the response code
                int responseCode = conn.getResponseCode();
                Log.d("CameraActivity", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response
                    StringBuilder responseBuilder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                    }

                    handleServerResponse(responseBuilder.toString());
                } else {
                    runOnUiThread(() -> Toast.makeText(CameraActivity.this, "Error: " + responseCode, Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                Log.e("CameraActivity", "Error: ", e);
                runOnUiThread(() -> Toast.makeText(CameraActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }).start();
    }



    private void handleServerResponse(String response) {
        Log.d("CameraActivity", "Raw Server Response: " + response); // Debugging

        try {
            JSONObject jsonResponse = new JSONObject(response);
            boolean isMatch = jsonResponse.optBoolean("match", false); // Avoid crash if "match" is missing
 
            runOnUiThread(() -> {
                if (isMatch) {
                    Log.d("CameraActivity", "Face Matched! Navigating to DashboardActivity.");
                    Intent intent = new Intent(CameraActivity.this, DashboardActivity.class);
                    intent.putExtra("NIC_NUMBER", nicNumber);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CameraActivity.this, "Face Not Matched!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            Log.e("CameraActivity", "Error parsing response: " + response, e);
            runOnUiThread(() -> Toast.makeText(CameraActivity.this, "Error processing server response.", Toast.LENGTH_SHORT).show());
        }
    }





}