package com.stockscanner.pivotal.pivotalstockscanner.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServiceApi {

    @FormUrlEncoded
    @POST("/get")
    Call<Quantity> consumeProduct(@Field("code") String productBarCode);

}
