package com.okhttp.get;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;


public class ClientGet {
    static OkHttpClient client = new OkHttpClient();

    public static void getPure(){
        Request request = new Request.Builder()
                .url("https://www.baidu.com")
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

    public static void getWithHeaders(){
        Request request = new Request.Builder()
                .url("http://localhost:8080")
                .get()
                .header("Env", "dev")
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

    public static void getWithQueryParams(){
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse("http://localhost:8080/search").newBuilder();
        urlBuilder.addQueryParameter("kw", "okhttp");

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Env", "dev")
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
        getPure();
        System.out.println("======分隔带======");
        getWithHeaders();
        System.out.println("======分隔带======");
        getWithQueryParams();
    }
}
