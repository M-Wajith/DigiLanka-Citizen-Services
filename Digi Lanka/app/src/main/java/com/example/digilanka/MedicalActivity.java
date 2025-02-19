package com.example.digilanka;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MedicalActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView timeSlotsRecyclerView;
    private TimeSlotAdapter timeSlotAdapter;
    private List<String> timeSlots;
    private String nicNumber;
    private int selectedYear, selectedMonth, selectedDay; // To hold selected date
    private String selectedMedicalCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);

        // Get NIC number from Intent
        nicNumber = getIntent().getStringExtra("NIC_NUMBER");

        // Initialize the medical center spinner
        Spinner medicalCenterSpinner = findViewById(R.id.medicalCenterSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.medical_centers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicalCenterSpinner.setAdapter(adapter);

        // Set listener for medical center selection
        medicalCenterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMedicalCenter = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case if needed, e.g., setting a default value
                selectedMedicalCenter = "Colombo"; // Default selection if none is made
            }
        });

        // Set up the back button to navigate back to DashboardActivity
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // This closes PassportActivity and returns to the previous activity (DashboardActivity)
        });

        // Initialize the calendar view and recycler view
        calendarView = findViewById(R.id.calendarView);
        timeSlotsRecyclerView = findViewById(R.id.timeSlotsRecyclerView);

        // Set up the RecyclerView
        timeSlots = new ArrayList<>();
        timeSlotAdapter = new TimeSlotAdapter(timeSlots, this::onTimeSlotSelected);
        timeSlotsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        timeSlotsRecyclerView.setAdapter(timeSlotAdapter);

        // Set the listener for date changes
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Save selected date
            selectedYear = year;
            selectedMonth = month;
            selectedDay = dayOfMonth;

            // Clear previous time slots
            timeSlots.clear();
            // Populate new time slots for the selected date
            populateTimeSlots(year, month, dayOfMonth);
        });
    }

    private void populateTimeSlots(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 8, 0); // Start from 8:00 AM

        // Clear previous time slots if any
        timeSlots.clear();

        // Create time slots from 8:00 AM to 3:30 PM
        while (calendar.get(Calendar.HOUR_OF_DAY) < 15 ||
                (calendar.get(Calendar.HOUR_OF_DAY) == 15 && calendar.get(Calendar.MINUTE) < 30)) {
            int startHour = calendar.get(Calendar.HOUR_OF_DAY);
            int startMinute = calendar.get(Calendar.MINUTE);

            // Calculate the end time by adding 30 minutes
            calendar.add(Calendar.MINUTE, 30);

            int endHour = calendar.get(Calendar.HOUR_OF_DAY);
            int endMinute = calendar.get(Calendar.MINUTE);

            // Format the time slot as "HH:mm - HH:mm"
            String timeSlot = String.format("%02d:%02d - %02d:%02d",
                    startHour, startMinute,
                    endHour, endMinute);
            timeSlots.add(timeSlot);
        }

        // Notify the adapter to update the RecyclerView
        timeSlotAdapter.notifyDataSetChanged();
    }

    private void onTimeSlotSelected(String timeSlot) {
        Toast.makeText(this, "Selected Time Slot: " + timeSlot, Toast.LENGTH_SHORT).show();
        // Here you can implement logic to save the appointment
        sendAppointmentRequest(timeSlot); // Call the method to send request
    }

    private void sendAppointmentRequest(String timeSlot) {
        // Parse the time slot to get start and end time
        String[] times = timeSlot.split(" - ");
        String startTime = times[0];
        String endTime = times[1];

        // Format start and end times with the selected date
        String formattedStartTime = formatDateTime(selectedYear, selectedMonth, selectedDay, startTime);
        String formattedEndTime = formatDateTime(selectedYear, selectedMonth, selectedDay, endTime);

        // Prepare the request data
        new SendAppointmentTask(nicNumber, formattedStartTime, formattedEndTime, selectedMedicalCenter).execute();
    }

    // Method to format date and time to the required format
    private String formatDateTime(int year, int month, int day, String time) {
        String[] timeParts = time.split(":"); // Split time by ":"
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        // Create a new Calendar instance for the selected date and time
        Calendar selectedDateCalendar = Calendar.getInstance();
        selectedDateCalendar.set(year, month, day, hour, minute, 0);

        // Format to YYYY-MM-DDTHH:mm:ss.sssZ
        return String.format("%04d-%02d-%02dT%02d:%02d:00.000Z",
                selectedDateCalendar.get(Calendar.YEAR),
                selectedDateCalendar.get(Calendar.MONTH) + 1, // Month is zero-based
                selectedDateCalendar.get(Calendar.DAY_OF_MONTH),
                selectedDateCalendar.get(Calendar.HOUR_OF_DAY),
                selectedDateCalendar.get(Calendar.MINUTE));
    }

    // Modify the AsyncTask to send the appointment request
    // Modify the AsyncTask to send the appointment request
    private class SendAppointmentTask extends AsyncTask<Void, Void, String> {
        private String nic;
        private String startTime;
        private String endTime;
        private String medicalCenter;

        // Include medicalCenter in the constructor parameters and initialize it
        public SendAppointmentTask(String nic, String startTime, String endTime, String medicalCenter) {
            this.nic = nic;
            this.startTime = startTime;
            this.endTime = endTime;
            this.medicalCenter = medicalCenter; // Initialize medicalCenter
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://10.0.2.2:5000/api/appointments");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Prepare JSON data to send in the request body
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("start_time", startTime);
                jsonParam.put("end_time", endTime);
                jsonParam.put("title", nic); // Using NIC number as the title
                jsonParam.put("medical_center", medicalCenter); // Add medical center

                // Log the JSON data to the console
                Log.d("SendAppointmentTask", "Sending JSON: " + jsonParam.toString());

                // Write the JSON data to the output stream
                conn.getOutputStream().write(jsonParam.toString().getBytes());

                // Check the response code
                if (conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                    return "Success";
                }
            } catch (Exception e) {
                Log.e("SendAppointmentTask", "Error sending appointment request", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the response from the server
            if (result != null) {
                Toast.makeText(MedicalActivity.this, "Appointment request sent successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MedicalActivity.this, "Failed to send appointment request", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
