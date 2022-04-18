package com.stardash.sportdash.network.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Methods {
    @GET
    Call<Model> getAllData(
            @Url String url
    );

}
