package com.ds.pokemon.driver

import com.ds.pokemon.domain.pokemon.PokemonResult
import com.ds.pokemon.domain.pokemonData.PokemonDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface RestApi {

    @GET("pokemon")
    suspend fun getPokemons(@QueryMap params: HashMap<String, Int>): Response<PokemonResult>

    @GET("pokemon/{pokemonId}")
    suspend fun getPokemonData(@Path("pokemonId") pokemonId: String): Response<PokemonDataModel>
}
