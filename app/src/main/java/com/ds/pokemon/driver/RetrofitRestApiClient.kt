package com.ds.pokemon.driver

import com.ds.pokemon.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitRestApiClient {
    fun create(): RestApi = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(RestApi::class.java)
}
