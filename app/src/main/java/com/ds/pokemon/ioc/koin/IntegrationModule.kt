package com.ds.pokemon.ioc.koin

import com.ds.pokemon.driver.RestApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val integrationModule = module {

    single<RestApi> {
        //todo move base url to build config
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(RestApi::class.java)
    }
}
