package com.ds.pokemon.ioc.koin

import com.ds.pokemon.driver.RetrofitRestApiClient
import org.koin.dsl.module


val integrationModule = module {
    /** provides RestApi client */
    single { RetrofitRestApiClient.create() }
}
