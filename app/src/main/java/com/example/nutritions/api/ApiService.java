package com.example.nutritions.api;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    String WOLFRAM_KEY = "UKT9JR-37JXTKHG9U";

    @GET("/v2/query?appid=" + WOLFRAM_KEY)
    Call<String> getNutrition(@Query("input") String input);
}
