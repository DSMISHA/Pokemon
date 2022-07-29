package com.ds.pokemon.domain

import com.ds.pokemon.presentation.pokemon_data.PokemonData

interface GetPockemonDataUseCase {
    suspend operator fun invoke(pokemonId: String): PokemonData
}
