package com.ds.pokemon.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PokemonModel(
    @SerializedName("name") @Expose val name: String?,
    @SerializedName("url") @Expose val url: String?
)

