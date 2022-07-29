package com.ds.pokemon.domain

import com.ds.pokemon.presentation.pokemon.Pokemon

/** Gets pokemons list data UseCase interface */
interface GetPokemonsUseCase {
    suspend operator fun invoke(): List<Pokemon>
}
