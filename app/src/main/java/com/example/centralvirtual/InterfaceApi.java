package com.example.centralvirtual;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface InterfaceApi {

    // endpoints "n6s16"
    @GET("posts")
    Call<List<responseServiceNumber>> getNumbers();

}
