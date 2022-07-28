package com.ds.pokemon.domain

import com.ds.pokemon.presentation.pokemon.Pokemon

interface GetPokemonsUseCase {
    suspend operator fun invoke(): List<Pokemon>
}
