package com.example.covid19;

import com.example.covid19.Models.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    String Base_URL = "https://corona.lmao.ninja/v2/";

    @GET("countries")
    Call<List<Model>> getCountryData();
}
