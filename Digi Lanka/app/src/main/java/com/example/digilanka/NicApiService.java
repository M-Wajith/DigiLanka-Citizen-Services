package com.example.digilanka;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NicApiService {
    @POST("nicapply")
    Call<ResponseBody> submitForm(@Body RequestBody requestBody);
}
