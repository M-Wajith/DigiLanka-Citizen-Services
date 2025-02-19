package com.example.digilanka;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NicThirdFragment extends Fragment {

    private static final int REQUEST_CODE_PHOTO_RECEIPT = 1;
    private static final int REQUEST_CODE_BIRTH_CERT_FRONT = 2;
    private static final int REQUEST_CODE_BIRTH_CERT_BACK = 3;
    private static final int REQUEST_CODE_POLICE_COMPLAINT = 4;

    private EditText editEmail;
    private TextView textUploadPhotoReceipt, textUploadBirthCertFront, textUploadBirthCertBack, textUploadComplaintBack;
    private Button buttonSubmit;
    private Button backButton;
    private boolean isPhotoReceiptUploaded = false;
    private boolean isBirthCertFrontUploaded = false;
    private boolean isBirthCertBackUploaded = false;
    private boolean isPoliceComplaintUploaded = false;

    private Uri imageUriPhotoReceipt;
    private Uri imageUriBirthCertFront;
    private Uri imageUriBirthCertBack;
    private Uri imageUriPoliceComplaint;

    private NicViewModel nicViewModel;

    private static final String TAG = "NicThirdFragment"; // Add a tag for logging

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nic_third, container, false);

        editEmail = view.findViewById(R.id.editEmail);
        textUploadPhotoReceipt = view.findViewById(R.id.textUploadPhotoReceipt);
        textUploadBirthCertFront = view.findViewById(R.id.textUploadBirthCertFront);
        textUploadBirthCertBack = view.findViewById(R.id.textUploadBirthCertBack);
        textUploadComplaintBack = view.findViewById(R.id.textUploadComplaintBack);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        backButton = view.findViewById(R.id.backButton);

        nicViewModel = new ViewModelProvider(requireActivity()).get(NicViewModel.class);

        // Set up click listeners for upload text views
        textUploadPhotoReceipt.setOnClickListener(v -> openGallery(REQUEST_CODE_PHOTO_RECEIPT));
        textUploadBirthCertFront.setOnClickListener(v -> openGallery(REQUEST_CODE_BIRTH_CERT_FRONT));
        textUploadBirthCertBack.setOnClickListener(v -> openGallery(REQUEST_CODE_BIRTH_CERT_BACK));
        textUploadComplaintBack.setOnClickListener(v -> openGallery(REQUEST_CODE_POLICE_COMPLAINT));

        buttonSubmit.setOnClickListener(v -> gatherDataAndSubmit());
        backButton.setOnClickListener(v -> handleBackButtonClick());

        return view;
    }

    private void openGallery(int requestCode) {
        Log.d(TAG, "Opening gallery for request code: " + requestCode);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && data != null) {
            Uri uri = data.getData();
            Log.d(TAG, "Received URI: " + uri + " for request code: " + requestCode);
            if (uri != null) {
                switch (requestCode) {
                    case REQUEST_CODE_PHOTO_RECEIPT:
                        imageUriPhotoReceipt = uri;
                        isPhotoReceiptUploaded = true;
                        textUploadPhotoReceipt.setText("Photo Receipt Uploaded");
                        break;
                    case REQUEST_CODE_BIRTH_CERT_FRONT:
                        imageUriBirthCertFront = uri;
                        isBirthCertFrontUploaded = true;
                        textUploadBirthCertFront.setText("Birth Certificate Front Uploaded");
                        break;
                    case REQUEST_CODE_BIRTH_CERT_BACK:
                        imageUriBirthCertBack = uri;
                        isBirthCertBackUploaded = true;
                        textUploadBirthCertBack.setText("Birth Certificate Back Uploaded");
                        break;
                    case REQUEST_CODE_POLICE_COMPLAINT:
                        imageUriPoliceComplaint = uri;
                        isPoliceComplaintUploaded = true;
                        textUploadComplaintBack.setText("Police Complaint Uploaded");
                        break;
                }
            }
            checkSubmitButtonState();
        } else {
            Log.e(TAG, "Failed to get result, resultCode: " + resultCode);
        }
    }

    private void checkSubmitButtonState() {
        boolean isEnabled = isPhotoReceiptUploaded &&
                isBirthCertFrontUploaded &&
                isBirthCertBackUploaded &&
                isPoliceComplaintUploaded &&
                isEmailFilled();
        buttonSubmit.setEnabled(isEnabled);
        Log.d(TAG, "Submit button enabled: " + isEnabled);
    }

    private boolean isEmailFilled() {
        boolean filled = !editEmail.getText().toString().trim().isEmpty();
        Log.d(TAG, "Is email filled: " + filled);
        return filled;
    }

    private void handleBackButtonClick() {
        Log.d(TAG, "Back button clicked");
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_nic, new NicSecondFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void gatherDataAndSubmit() {
        Log.d(TAG, "Gathering data to submit");
        // Set the data from the ViewModel
        nicViewModel.setEmail(editEmail.getText().toString().trim());
        nicViewModel.setPhotoReceipt(convertImageToBase64(imageUriPhotoReceipt));
        nicViewModel.setBirthCertFront(convertImageToBase64(imageUriBirthCertFront));
        nicViewModel.setBirthCertBack(convertImageToBase64(imageUriBirthCertBack));
        nicViewModel.setPoliceComplaint(convertImageToBase64(imageUriPoliceComplaint));

        String name = nicViewModel.getName().getValue();
        Log.d(TAG, "name from viewmodel " + name);
        JSONObject jsonObject = new JSONObject();
        try {
            // Collect data from the ViewModel for all fields with the correct names
            jsonObject.put("name", nicViewModel.getName().getValue());
            jsonObject.put("surname", nicViewModel.getSurname().getValue());
            jsonObject.put("dateOfBirth", nicViewModel.getDateOfBirth().getValue());
            jsonObject.put("birthCertNumber", nicViewModel.getBirthCertNumber().getValue());
            jsonObject.put("gender", nicViewModel.getGender().getValue());
            jsonObject.put("oldNicNumber", nicViewModel.getOldNic().getValue());
            jsonObject.put("civilStatus", nicViewModel.getCivilStatus().getValue());
            jsonObject.put("profession", nicViewModel.getProfession().getValue());
            jsonObject.put("birthPlace", nicViewModel.getBirthPlace().getValue());
            jsonObject.put("district", nicViewModel.getDistrict().getValue());
            jsonObject.put("permanentAddress", nicViewModel.getPermanentAddress().getValue());
            jsonObject.put("phoneNumber", nicViewModel.getMobileNumber().getValue());
            jsonObject.put("email", nicViewModel.getEmail().getValue());
            jsonObject.put("photoReceipt", nicViewModel.getPhotoReceipt().getValue());
            jsonObject.put("birthCertFront", nicViewModel.getBirthCertFront().getValue());
            jsonObject.put("birthCertBack", nicViewModel.getBirthCertBack().getValue());
            jsonObject.put("policeComplaint", nicViewModel.getPoliceComplaint().getValue());

            Log.d(TAG, "JSON Object for submission: " + jsonObject.toString());

            String jsonData = jsonObject.toString();
            sendDataToBackend(jsonData);
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception: " + e.getMessage(), e);
        }
    }

    private void sendDataToBackend(String jsonData) {
        Log.d(TAG, "Sending data to backend");
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        NicApiService nicApiService = retrofit.create(NicApiService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonData);
        Call<ResponseBody> call = nicApiService.submitForm(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Data submitted successfully: " + response.message());
                    Toast.makeText(getActivity(), "Data submitted successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Log.e(TAG, "Failed to submit data. Code: " + response.code());
                    try {
                        Log.e(TAG, "Error body: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Failed to submit data: " + t.getMessage(), t);
                Toast.makeText(getActivity(), "Failed to submit data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertImageToBase64(Uri uri) {
        if (uri == null) return null;
        try (InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
        } catch (Exception e) {
            Log.e(TAG, "Error converting image to Base64: " + e.getMessage(), e);
            return null;
        }
    }
}
