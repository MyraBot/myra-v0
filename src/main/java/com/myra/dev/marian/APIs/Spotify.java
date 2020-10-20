package com.myra.dev.marian.APIs;

import okhttp3.*;
import org.json.JSONObject;

public class Spotify {
    public String accessToken;

    public void authorizationToken() throws Exception {
        OkHttpClient client = new OkHttpClient();
        //form parameters
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();
        //build request
        Request request = new Request.Builder()
                .addHeader("Authorization", "ZjE5YmYwYTdjYjIwNGMwOThkYmRhYWVlZGY0N2Y4NDI6ZDRkNDhiMmU0YjQ3NGQwOThmYTQ0MGE2ZDAxZWNlNDI")
                .url("https://accounts.spotify.com/api/token")
                .post(body)
                .build();
        //make request
        try (Response response = client.newCall(request).execute()) {
            System.out.println(response);
            //return access token
            String output = response.body().string();
            JSONObject obj = new JSONObject(output);
            System.out.println(obj.getString("expires_in"));
            accessToken = obj.getString("access_token");
        }
    }
}

