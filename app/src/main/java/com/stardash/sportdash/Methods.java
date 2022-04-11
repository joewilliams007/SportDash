package com.stardash.sportdash;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Methods {
    @GET
    Call<Model> getAllData(
            @Url String url
    );

}
