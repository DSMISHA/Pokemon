package com.ds.pokemon.ioc.koin

import com.ds.pokemon.domain.GetPokemonDataUseCase
import com.ds.pokemon.domain.GetPokemonsUseCase
import com.ds.pokemon.domain.RestGetPokemonDataUseCase
import com.ds.pokemon.domain.RestGetPokemonsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    /** Provides GetPokemonsUseCase for loading pokemons list */
    single<GetPokemonsUseCase> { RestGetPokemonsUseCase(get()) }

    /** Provides GetPokemonDataUseCase for loading pokemon extended data */
    single<GetPokemonDataUseCase> { RestGetPokemonDataUseCase(get()) }
}
