package com.example.digilanka;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LicenseStatusFragment extends Fragment {

    private static final String ARG_NIC_NUMBER = "nic_number"; // Key for the NIC number argument
    private String nicNumber; // Variable to store the NIC number

    // TextViews for displaying appointment statuses
    private TextView medicalStatus;
    private TextView writtenStatus; // TextView for written appointment status
    private TextView practicalStatus; // TextView for practical appointment status
    private TextView licenseRequested, licenseStatus; // Assuming you have TextViews for license status

    public static LicenseStatusFragment newInstance(String nicNumber) {
        LicenseStatusFragment fragment = new LicenseStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NIC_NUMBER, nicNumber); // Store the NIC number
        fragment.setArguments(args); // Set the arguments for the fragment
        return fragment;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_license_status, container, false);

        // Retrieve the NIC number from arguments
        if (getArguments() != null) {
            nicNumber = getArguments().getString(ARG_NIC_NUMBER);
        }

        // Initialize TextViews
        medicalStatus = view.findViewById(R.id.medicalStatus);
        writtenStatus = view.findViewById(R.id.writtenStatus);
        practicalStatus = view.findViewById(R.id.practicalStatus); // Initialize practical status TextView


        // Fetch and display data

        fetchMedicalAppointmentStatus(nicNumber); // Pass the NIC number to the fetching method
        fetchLicenseStatus(nicNumber); // Fetch the license status
        fetchWrittenAppointmentStatus(nicNumber);
        fetchPracticalAppointmentStatus(nicNumber); // Fetch practical appointment status

        return view;
    }

    // Fetch and display the medical appointment status based on NIC
    private void fetchMedicalAppointmentStatus(String nic) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:5000/api/appointments/status/" + nic);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse response JSON
                JSONObject appointment = new JSONObject(response.toString());

                // Run UI updates on the main thread
                getActivity().runOnUiThread(() -> {
                    try {
                        String startTime = appointment.getString("start_time");
                        String endTime = appointment.getString("end_time");
                        String status = appointment.getString("status");

                        // Update the TextViews
                        medicalStatus.setText("Requested: " + startTime + " - " + endTime);
                        medicalStatus.setText("Status: " + (status.equals("requested") ? "Requested" : status.equals("booked") ? "Accepted" : status));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void fetchWrittenAppointmentStatus(String nic) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:5000/appointments/written/status/" + nic); // Adjust the URL for written appointment status
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse response JSON
                JSONObject appointment = new JSONObject(response.toString());

                // Run UI updates on the main thread
                getActivity().runOnUiThread(() -> {
                    try {
                        String startTime = appointment.getString("start_time");
                        String endTime = appointment.getString("end_time");
                        String status = appointment.getString("status");

                        // Update the TextView for written appointment status
                        writtenStatus.setText("Requested: " + startTime + " - " + endTime);
                        writtenStatus.setText("Status: " + (status.equals("requested") ? "Requested" : status.equals("booked") ? "Accepted" : status));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void fetchPracticalAppointmentStatus(String nic) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:5000/practical_appointments/status/" + nic); // Adjust the URL for practical appointment status
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse response JSON
                JSONObject appointment = new JSONObject(response.toString());

                // Run UI updates on the main thread
                getActivity().runOnUiThread(() -> {
                    try {
                        String startTime = appointment.getString("start_time");
                        String endTime = appointment.getString("end_time");
                        String status = appointment.getString("status");

                        // Update the TextView for practical appointment status
                        practicalStatus.setText("Requested: " + startTime + " - " + endTime);
                        practicalStatus.setText("Status: " + (status.equals("requested") ? "Requested" : status.equals("booked") ? "Accepted" : status));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }



    // Fetch and display the license status based on NIC
    private void fetchLicenseStatus(String nic) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:5000/api/license/status/" + nic); // Adjust the URL as needed
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse response JSON
                JSONObject license = new JSONObject(response.toString());

                // Run UI updates on the main thread
                getActivity().runOnUiThread(() -> {
                    try {
                        String licenseStatusString = license.getString("status"); // Adjust this based on your API response
                        String licenseRequestedString = license.getString("requested"); // Adjust based on your API response

                        // Update the TextViews
                        licenseRequested.setText("Requested: " + licenseRequestedString);
                        licenseStatus.setText("Status: " + licenseStatusString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
