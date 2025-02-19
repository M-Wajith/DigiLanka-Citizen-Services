package com.example.digilanka;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PassportApi {

    // Define the API endpoint and use @Body to accept the JSON payload
    @POST("passportapply") // Ensure this matches your actual backend endpoint
    Call<ResponseBody> submitForm(
            @Body RequestBody jsonData
    );

    // Uncomment and define a structured response if needed
    // Call<YourResponseModel> submitForm(
    //         @Body RequestBody jsonData
    // );
}
