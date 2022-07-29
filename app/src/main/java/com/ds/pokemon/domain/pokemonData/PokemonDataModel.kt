package com.ds.pokemon.domain.pokemonData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PokemonDataModel(
    @SerializedName("abilities") @Expose val abilities: List<AbilityModel>,
    @SerializedName("sprites") @Expose val sprites: SpritesModel?
)

data class SpritesModel(
    @SerializedName("other") @Expose val other: OtherModel?
)

data class OtherModel(
    @SerializedName("official-artwork") @Expose val officialArtwork: OfficialArtWorkModel?
)

data class OfficialArtWorkModel(
    @SerializedName("front_default") @Expose val frontDefault: String?
)

data class AbilityModel(
    @SerializedName("ability") @Expose val abilityName: AbilityNameModel?
)

data class AbilityNameModel(
    @SerializedName("name") @Expose val name: String?
)
