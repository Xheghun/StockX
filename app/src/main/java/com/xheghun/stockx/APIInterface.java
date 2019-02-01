package com.xheghun.stockx;

import com.xheghun.stockx.request.CryptoList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface APIInterface {

    @Headers("X-CMC_PRO_API_KEY: dd41d785-df93-4129-8b2c-71e998b83b07")
    @GET("/v1/cryptocurrency/listings/latest?")
    Call<CryptoList> getList(@Query("limit") String rows);
}
