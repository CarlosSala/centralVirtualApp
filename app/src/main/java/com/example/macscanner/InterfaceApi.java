package com.example.macscanner;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface InterfaceApi {

    // endpoints "n6s16"
    @GET("n6s16")
    Call<List<responseServiceNumber>> getNumbers();

}
