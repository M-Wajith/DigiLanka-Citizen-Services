package com.example.digilanka;

// ApiService.java
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // Check if the passport application is applied (NIC number exists in citizen_passport_application)
    @GET("application/status/applied/{nic_number}")
    Call<PassportResponse> checkApplicationExists(@Path("nic_number") String nicNumber);

    // Check if the passport application is verified
    @GET("application/status/verified/{nic_number}")
    Call<PassportResponse> checkVerified(@Path("nic_number") String nicNumber);

    // Check if the passport application is printed
    @GET("application/status/printed/{nic_number}")
    Call<PassportResponse> checkPrinted(@Path("nic_number") String nicNumber);

    // Check if the passport application is dispatched
    @GET("application/status/dispatched/{nic_number}")
    Call<PassportResponse> checkDispatched(@Path("nic_number") String nicNumber);

    Call<ResponseBody> submitNicData(RequestBody requestBody);

    @POST("dispatched_passport/received")
    Call<Void> sendPassportReceivedStatus(@Body PassportStatusFragment.PassportReceivedRequest request);

}
