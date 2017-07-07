package com.stockscanner.pivotal.pivotalstockscanner.api;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceImpl {

    private final ServiceApi service;

    public ServiceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://snack-foundry-be.cfapps.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ServiceApi.class);
    }

    public int getStockQuantity(String productBarCode) {
        try {
            return service.consumeProduct(productBarCode).execute().body().getQty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
