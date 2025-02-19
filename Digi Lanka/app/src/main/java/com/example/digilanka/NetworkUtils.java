package com.example.digilanka;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.IOException;

public class NetworkUtils {

    private static final String BASE_URL = "http://10.0.2.2:5000/check_nic";

    public static void checkNIC(String nicNumber, final NICCallback callback) {
        OkHttpClient client = new OkHttpClient();

        // Build the URL with the NIC number as a query parameter
        String urlWithNIC = BASE_URL + "?nic_number=" + nicNumber;

        Request request = new Request.Builder()
                .url(urlWithNIC)  // Use the URL with query parameter
                .get()  // Change to GET method
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    // Parse the JSON response
                    try {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);
                        boolean exists = jsonObject.get("exists").getAsBoolean();

                        if (exists) {
                            callback.onSuccess("NIC validated!");  // NIC exists
                        } else {
                            callback.onError("NIC not verified!");  // NIC does not exist
                        }
                    } catch (JsonParseException e) {
                        callback.onError("Error parsing response: " + e.getMessage());
                    }
                } else {
                    callback.onError(response.message());
                }
            }
        });
    }

    public interface NICCallback {
        void onSuccess(String response);
        void onError(String error);
    }
}
