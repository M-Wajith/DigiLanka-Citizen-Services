package com.example.digilanka;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NicStatusApi {

    // Check if NIC application exists
    @GET("application/status/applied/{nicNumber}")
    Call<NicResponse> checkNicApplicationExists(@Path("nicNumber") String nicNumber);

    // Check if NIC application is verified
    @GET("application/status/verified/{nicNumber}")
    Call<NicResponse> checkNicVerified(@Path("nicNumber") String nicNumber);

    // Check if NIC application is printed
    @GET("application/status/printed/{nicNumber}")
    Call<NicResponse> checkNicPrinted(@Path("nicNumber") String nicNumber);

    // Check if NIC application is dispatched
    @GET("application/status/dispatched/{nicNumber}")
    Call<NicResponse> checkNicDispatched(@Path("nicNumber") String nicNumber);
}
