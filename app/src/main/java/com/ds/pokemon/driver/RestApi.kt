package com.ds.pokemon.driver

import com.ds.pokemon.domain.PokemonResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RestApi {

    @GET("pokemon")
    suspend fun getPokemons(@QueryMap params: HashMap<String, Int>): Response<PokemonResult>

}
