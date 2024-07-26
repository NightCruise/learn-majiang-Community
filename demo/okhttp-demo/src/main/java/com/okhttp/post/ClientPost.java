package com.okhttp.post;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

public class ClientPost {
    static OkHttpClient client = new OkHttpClient();

    public static void postWithBody(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "yx");
        jsonObject.put("age", 23);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                jsonObject.toString()
        );

        Request request = new Request.Builder()
                .url("http://localhost:8080/insert")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();

            System.out.println("[headers]" + response.headers());
            System.out.println("[status code]" + response.code());
            System.out.println("[body]" + Objects.requireNonNull(response.body()).string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        postWithBody();
    }
}
