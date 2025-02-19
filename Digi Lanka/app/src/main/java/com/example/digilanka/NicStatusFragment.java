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

public class NicStatusFragment extends Fragment {

    private static final String ARG_NIC_NUMBER = "nic_number";
    private String nicNumber;
    private NicStatusItem statusApplied;
    private NicStatusItem statusVerified;
    private NicStatusItem statusPrinted;
    private NicStatusItem statusDispatched;

    // Flags to track if each status has been checked
    private boolean isAppliedChecked = false;
    private boolean isVerifiedChecked = false;
    private boolean isPrintedChecked = false;
    private boolean isDispatchedChecked = false;

    public static NicStatusFragment newInstance(String nicNumber) {
        NicStatusFragment fragment = new NicStatusFragment();
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
        View view = inflater.inflate(R.layout.fragment_nic_status, container, false);

        // Initialize the NicStatusItem views
        statusApplied = view.findViewById(R.id.status_applied);
        statusVerified = view.findViewById(R.id.status_verified);
        statusPrinted = view.findViewById(R.id.status_printed);
        statusDispatched = view.findViewById(R.id.status_delivered);

        // Start checking the NIC application status
        checkNicApplicationStatus(nicNumber);

        return view;
    }

    private void checkNicApplicationStatus(String nicNumber) {
        Log.d("NicStatusFragment", "Checking NIC application status for NIC number: " + nicNumber);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/") // Update the base URL as needed
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NicStatusApi nicStatusApi = retrofit.create(NicStatusApi.class);

        // Check if NIC number exists in citizen_nic_application
        Call<NicResponse> callApplicationStatus = nicStatusApi.checkNicApplicationExists(nicNumber);
        callApplicationStatus.enqueue(new Callback<NicResponse>() {
            @Override
            public void onResponse(Call<NicResponse> call, Response<NicResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isExists = response.body().isExists();
                    statusApplied.setStatusCompleted(isExists);  // Update Applied status
                    isAppliedChecked = true;  // Mark applied status as checked

                    if (isExists) {
                        // Check the next status
                        checkVerifiedStatus(nicNumber);
                    } else {
                        Toast.makeText(getContext(), "NIC number not found in applications.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<NicResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkVerifiedStatus(String nicNumber) {
        Log.d("NicStatusFragment", "Checking verified status for NIC number: " + nicNumber);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NicStatusApi nicStatusApi = retrofit.create(NicStatusApi.class);

        // Check if verified status has already been checked
        if (isVerifiedChecked) return;

        Call<NicResponse> callVerified = nicStatusApi.checkNicVerified(nicNumber);
        callVerified.enqueue(new Callback<NicResponse>() {
            @Override
            public void onResponse(Call<NicResponse> call, Response<NicResponse> response) {
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
            public void onFailure(Call<NicResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPrintedStatus(String nicNumber) {
        Log.d("NicStatusFragment", "Checking printed status for NIC number: " + nicNumber);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NicStatusApi nicStatusApi = retrofit.create(NicStatusApi.class);

        // Check if printed status has already been checked
        if (isPrintedChecked) return;

        Call<NicResponse> callPrinted = nicStatusApi.checkNicPrinted(nicNumber);
        callPrinted.enqueue(new Callback<NicResponse>() {
            @Override
            public void onResponse(Call<NicResponse> call, Response<NicResponse> response) {
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
            public void onFailure(Call<NicResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkDispatchedStatus(String nicNumber) {
        Log.d("NicStatusFragment", "Checking dispatched status for NIC number: " + nicNumber);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NicStatusApi nicStatusApi = retrofit.create(NicStatusApi.class);

        // Check if dispatched status has already been checked
        if (isDispatchedChecked) return;

        Call<NicResponse> callDispatched = nicStatusApi.checkNicDispatched(nicNumber);
        callDispatched.enqueue(new Callback<NicResponse>() {
            @Override
            public void onResponse(Call<NicResponse> call, Response<NicResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isDispatched = response.body().isExists();
                    statusDispatched.setStatusCompleted(isDispatched);  // Update Dispatched status
                    isDispatchedChecked = true;  // Mark dispatched status as checked
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<NicResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleError(Response<?> response) {
        String errorMessage;
        try {
            errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
        } catch (IOException e) {
            errorMessage = "Error reading response.";
            e.printStackTrace();
        }
        Log.e("NicStatusFragment", "Failed to fetch status: " + errorMessage);
    }
}
