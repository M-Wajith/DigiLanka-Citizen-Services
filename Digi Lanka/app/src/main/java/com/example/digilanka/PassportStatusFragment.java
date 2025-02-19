package com.example.digilanka;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.app.AlertDialog; // Import AlertDialog
import android.content.DialogInterface; // Import DialogInterface

public class PassportStatusFragment extends Fragment {

    private static final String ARG_NIC_NUMBER = "nic_number";
    private String nicNumber;
    private PassportStatusItem statusApplied;
    private PassportStatusItem statusVerified;
    private PassportStatusItem statusPrinted;
    private PassportStatusItem statusDispatched;

    // Flags to track if each status has been checked
    private boolean isAppliedChecked = false;
    private boolean isVerifiedChecked = false;
    private boolean isPrintedChecked = false;
    private boolean isDispatchedChecked = false;

    public static PassportStatusFragment newInstance(String nicNumber) {
        PassportStatusFragment fragment = new PassportStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NIC_NUMBER, nicNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nicNumber = getArguments().getString(ARG_NIC_NUMBER);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passport_status, container, false);

        // Initialize the PassportStatusItem views
        statusApplied = view.findViewById(R.id.status_applied);
        statusVerified = view.findViewById(R.id.status_verified);
        statusPrinted = view.findViewById(R.id.status_printed);
        statusDispatched = view.findViewById(R.id.status_delivered);

        // Start checking the application status
        checkApplicationStatus(nicNumber);

        return view;
    }

    private void checkApplicationStatus(String nicNumber) {
        Log.d("PassportStatusFragment", "Checking application status for NIC number: " + nicNumber);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/passport/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Check if NIC number exists in citizen_passport_application
        Call<PassportResponse> callApplicationStatus = apiService.checkApplicationExists(nicNumber);
        callApplicationStatus.enqueue(new Callback<PassportResponse>() {
            @Override
            public void onResponse(Call<PassportResponse> call, Response<PassportResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isExists = response.body().isExists();
                    statusApplied.setStatusCompleted(isExists);  // Update Applied status
                    isAppliedChecked = true;  // Mark applied status as checked

                    if (isExists) {
                        // Only check the next status if not already checked
                        if (!isVerifiedChecked) {
                            checkVerifiedStatus(nicNumber);
                        }
                    } else {
                        Toast.makeText(getContext(), "NIC number not found in applications.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<PassportResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkVerifiedStatus(String nicNumber) {
        Log.d("PassportStatusFragment", "Checking verified status for NIC number: " + nicNumber);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/passport/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Check if verified status has already been checked
        if (isVerifiedChecked) return;

        Call<PassportResponse> callVerified = apiService.checkVerified(nicNumber);
        callVerified.enqueue(new Callback<PassportResponse>() {
            @Override
            public void onResponse(Call<PassportResponse> call, Response<PassportResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isVerified = response.body().isExists();
                    statusVerified.setStatusCompleted(isVerified);  // Update Verified status
                    isVerifiedChecked = true;  // Mark verified status as checked
                    if (isVerified) {
                        checkPrintedStatus(nicNumber);  // Check Printed status
                    }
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<PassportResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPrintedStatus(String nicNumber) {
        Log.d("PassportStatusFragment", "Checking printed status for NIC number: " + nicNumber);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/passport/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Check if printed status has already been checked
        if (isPrintedChecked) return;

        Call<PassportResponse> callPrinted = apiService.checkPrinted(nicNumber);
        callPrinted.enqueue(new Callback<PassportResponse>() {
            @Override
            public void onResponse(Call<PassportResponse> call, Response<PassportResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isPrinted = response.body().isExists();
                    statusPrinted.setStatusCompleted(isPrinted);  // Update Printed status
                    isPrintedChecked = true;  // Mark printed status as checked
                    if (isPrinted) {
                        checkDispatchedStatus(nicNumber);  // Check Dispatched status
                    }
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<PassportResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkDispatchedStatus(String nicNumber) {
        Log.d("PassportStatusFragment", "Checking dispatched status for NIC number: " + nicNumber);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/passport/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Check if dispatched status has already been checked
        if (isDispatchedChecked) return;

        Call<PassportResponse> callDispatched = apiService.checkDispatched(nicNumber);
        callDispatched.enqueue(new Callback<PassportResponse>() {
            @Override
            public void onResponse(Call<PassportResponse> call, Response<PassportResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isDispatched = response.body().isExists();
                    statusDispatched.setStatusCompleted(isDispatched);  // Update Dispatched status
                    isDispatchedChecked = true;  // Mark dispatched status as checked

                    // Show the dialog asking if the user received the passport
                    showPassportReceivedDialog();
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<PassportResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showPassportReceivedDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Passport Status")
                .setMessage("Did you get the Passport?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle "Yes" response
                        sendPassportReceivedStatus(true); // Send "yes" status to the backend
                        Toast.makeText(getContext(), "Great! Enjoy your passport!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle "No" response
                        sendPassportReceivedStatus(false); // Send "no" status to the backend
                        Toast.makeText(getContext(), "Please check with the service center.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(true)
                .show();
    }


    private void sendPassportReceivedStatus(boolean isReceived) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/") // Your base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the request object
        PassportReceivedRequest request = new PassportReceivedRequest(nicNumber, isReceived);

        // Create the API interface for the sending status
        Call<Void> call = retrofit.create(ApiService.class).sendPassportReceivedStatus(request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Status sent to server successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error sending status: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class PassportReceivedRequest {
        private String nic_number;
        private boolean confirmation_status;

        public PassportReceivedRequest(String nicNumber, boolean confirmationStatus) {
            this.nic_number = nicNumber;
            this.confirmation_status = confirmationStatus;
        }
    }




    private void handleError(Response<?> response) {
        String errorMessage;
        try {
            errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
        } catch (IOException e) {
            errorMessage = "Error reading response.";
            e.printStackTrace();
        }
        Log.e("PassportStatusFragment", "Failed to fetch status: " + errorMessage);
    }
}
