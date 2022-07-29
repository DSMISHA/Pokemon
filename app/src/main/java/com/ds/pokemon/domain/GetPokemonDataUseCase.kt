package com.ds.pokemon.domain

import com.ds.pokemon.presentation.pokemon_data.PokemonData


/** Gets pokemon extended data UseCase interface */
interface GetPokemonDataUseCase {
    suspend operator fun invoke(pokemonId: String): PokemonData
}
