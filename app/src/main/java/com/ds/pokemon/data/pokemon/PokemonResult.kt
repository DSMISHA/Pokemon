package com.ds.pokemon.data.pokemon

import com.ds.pokemon.domain.PokemonModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PokemonResult(
    @SerializedName("count") @Expose val count: Int,
    @SerializedName("next") @Expose val next: String,
    @SerializedName("previous") @Expose val previous: String,
    @SerializedName("results") @Expose val results: List<PokemonModel>?
)
