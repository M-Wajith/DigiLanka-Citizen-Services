package com.example.digilanka;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class ThirdFragment extends Fragment {
    private PassportViewModel passportViewModel;
    private Button buttonSubmit, backButton;
    private TextView textUploadNICFront, textUploadNICBack, textUploadBirthCertFront, textUploadBirthCertBack, textUploadPhotoReceipt;
    private EditText editMobileNumber, editEmail;

    private Uri selectedImageNICFront, selectedImageNICBack, selectedImageBirthCertFront, selectedImageBirthCertBack, selectedImageReceipt;

    private static final int PICK_NIC_FRONT_REQUEST = 1;
    private static final int PICK_NIC_BACK_REQUEST = 2;
    private static final int PICK_BIRTH_CERT_FRONT_REQUEST = 3;
    private static final int PICK_BIRTH_CERT_BACK_REQUEST = 4;
    private static final int PICK_RECEIPT_REQUEST = 5;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        passportViewModel = new ViewModelProvider(requireActivity()).get(PassportViewModel.class);

        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        backButton = view.findViewById(R.id.backButton);
        textUploadNICFront = view.findViewById(R.id.textUploadNICFront);
        textUploadNICBack = view.findViewById(R.id.textUploadNICBack);
        textUploadBirthCertFront = view.findViewById(R.id.textUploadBirthCertFront);
        textUploadBirthCertBack = view.findViewById(R.id.textUploadBirthCertBack);
        textUploadPhotoReceipt = view.findViewById(R.id.textUploadPhotoReceipt);
        editMobileNumber = view.findViewById(R.id.editMobileNumber);
        editEmail = view.findViewById(R.id.editEmail);

        // Set up listeners for uploading images
        textUploadNICFront.setOnClickListener(v -> openImagePicker(PICK_NIC_FRONT_REQUEST));
        textUploadNICBack.setOnClickListener(v -> openImagePicker(PICK_NIC_BACK_REQUEST));
        textUploadBirthCertFront.setOnClickListener(v -> openImagePicker(PICK_BIRTH_CERT_FRONT_REQUEST));
        textUploadBirthCertBack.setOnClickListener(v -> openImagePicker(PICK_BIRTH_CERT_BACK_REQUEST));
        textUploadPhotoReceipt.setOnClickListener(v -> openImagePicker(PICK_RECEIPT_REQUEST));

        buttonSubmit.setOnClickListener(v -> validateAndSubmit());
        backButton.setOnClickListener(v -> getActivity().onBackPressed());

        return view;
    }

    private void openImagePicker(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            switch (requestCode) {
                case PICK_NIC_FRONT_REQUEST:
                    selectedImageNICFront = imageUri;
                    break;
                case PICK_NIC_BACK_REQUEST:
                    selectedImageNICBack = imageUri;
                    break;
                case PICK_BIRTH_CERT_FRONT_REQUEST:
                    selectedImageBirthCertFront = imageUri;
                    break;
                case PICK_BIRTH_CERT_BACK_REQUEST:
                    selectedImageBirthCertBack = imageUri;
                    break;
                case PICK_RECEIPT_REQUEST:
                    selectedImageReceipt = imageUri;
                    break;
            }
        }
    }

    private void validateAndSubmit() {
        String mobileNumber = editMobileNumber.getText().toString().trim();
        String emailAddress = editEmail.getText().toString().trim();

        // Log mobile number and email
        Log.d("FormData", "Mobile Number: " + mobileNumber);
        Log.d("FormData", "Email Address: " + emailAddress);

        if (mobileNumber.isEmpty() || emailAddress.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        // Set mobile number and email in ViewModel
        passportViewModel.setPhoneNumber(mobileNumber);
        passportViewModel.setEmailAddress(emailAddress);

        String nicFrontBase64 = convertImageToBase64(selectedImageNICFront);
        String nicBackBase64 = convertImageToBase64(selectedImageNICBack);
        String birthCertFrontBase64 = convertImageToBase64(selectedImageBirthCertFront);
        String birthCertBackBase64 = convertImageToBase64(selectedImageBirthCertBack);
        String receiptBase64 = convertImageToBase64(selectedImageReceipt);

        String travelDocument = passportViewModel.getTravelDocument().getValue().trim();
        if (travelDocument.isEmpty()) {
            Toast.makeText(getContext(), "Please select a travel document", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("TravelDocument", "Value being sent: " + travelDocument);

        try {
            // Create JSON object for submission
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("travelDocument", travelDocument);
            jsonObject.put("surname", passportViewModel.getSurname().getValue());
            jsonObject.put("otherName", passportViewModel.getOtherName().getValue());
            jsonObject.put("nicNumber", passportViewModel.getNicNumber().getValue());
            jsonObject.put("permanentAddress", passportViewModel.getPermanentAddress().getValue());
            jsonObject.put("district", passportViewModel.getDistrict().getValue());
            jsonObject.put("dateOfBirth", passportViewModel.getDateOfBirth().getValue());
            jsonObject.put("birthCertificateNumber", passportViewModel.getBirthCertificateNumber().getValue());
            jsonObject.put("gender", passportViewModel.getGender().getValue());
            jsonObject.put("profession", passportViewModel.getProfession().getValue());
            jsonObject.put("dualCitizenshipNumber", passportViewModel.getDualCitizenshipNumber().getValue());
            jsonObject.put("phoneNumber", passportViewModel.getPhoneNumber().getValue());
            jsonObject.put("emailAddress", passportViewModel.getEmailAddress().getValue());
            jsonObject.put("nicFrontImage", nicFrontBase64);
            jsonObject.put("nicBackImage", nicBackBase64);
            jsonObject.put("birthCertificateFront", birthCertFrontBase64);
            jsonObject.put("birthCertificateBack", birthCertBackBase64);
            jsonObject.put("photoReceiptImage", receiptBase64);

            // Log the JSON object
            Log.d("FormData", "JSON Object for submission: " + jsonObject.toString());

            // Submit data to the server
            submitData(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error occurred while submitting data", Toast.LENGTH_SHORT).show();
        }
    }

    private String convertImageToBase64(Uri imageUri) {
        if (imageUri == null) return null;
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
            byte[] buffer = new byte[8192];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void submitData(String jsonData) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/") // Ensure your server is accessible from the emulator
                .addConverterFactory(GsonConverterFactory.create())
                .client(client) // Set the client here
                .build();

        PassportApi passportApi = retrofit.create(PassportApi.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
        Call<ResponseBody> call = passportApi.submitForm(requestBody); // Ensure this method matches your API interface.

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    try {
                        Log.d("PassportAppResponse", "Success: " + response.body().string());
                        Toast.makeText(getContext(), "Submission successful!", Toast.LENGTH_SHORT).show();
                        // After successful submission, navigate back to DashboardActivity
                        Intent intent = new Intent(getContext(), DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                        // Optionally, finish the current activity if you want to close it
                        ((Activity) getContext()).finish();


                    } catch (IOException e) {
                        Log.e("PassportAppError", "Error reading response body", e);
                        Toast.makeText(getContext(), "Error reading response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle error response
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.d("FormData", "Request body being sent: " + requestBody.toString());
                        Log.e("PassportAppError", "Error: " + errorBody);
                        Toast.makeText(getContext(), "Submission failed: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("PassportAppError", "Error reading error body", e);
                        Toast.makeText(getContext(), "Error reading error response", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("FormSubmission", "Failure: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to submit: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private MultipartBody.Part createPartFromUri(Uri fileUri, String partName) {
        if (fileUri == null) {
            Log.e("CreatePartFromUri", "File URI is null for part: " + partName);
            return null; // Handle case where fileUri is null
        }

        // Log the file path for debugging
        Log.d("CreatePartFromUri", "File URI: " + fileUri.toString());

        // Convert URI to file
        String filePath = getRealPathFromURI(fileUri);
        if (filePath == null) {
            Log.e("CreatePartFromUri", "File path is null for URI: " + fileUri.toString());
            return null; // Handle case where file path is null
        }

        File file = new File(filePath);
        if (!file.exists()) {
            Log.e("CreatePartFromUri", "File does not exist: " + file.getAbsolutePath());
            return null; // Handle case where file does not exist
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private String getRealPathFromURI(Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }

        return filePath;
    }



}
