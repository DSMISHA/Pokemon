package com.ds.pokemon.presentation.pokemon_data


data class PokemonData(
    val abilities: List<Ability>,
    val sprites: Sprites?
)

data class Sprites(val other: Other?)

data class Other(val officialArtwork: OfficialArtWork?)

data class OfficialArtWork(val frontDefault: String?)

data class Ability(val name: String)
