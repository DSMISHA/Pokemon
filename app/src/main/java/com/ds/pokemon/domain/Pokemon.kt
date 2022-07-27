package com.ds.pokemon.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("name") @Expose val name: String?,
    @SerializedName("url") @Expose val url: String?
)

data class PokemonResult(
    @SerializedName("count") @Expose val count: Int,
    @SerializedName("next") @Expose val next: String,
    @SerializedName("previous") @Expose val previous: String,
    @SerializedName("results") @Expose val results: List<Pokemon>?
)
