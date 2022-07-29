package com.ds.pokemon.ioc.koin

import com.ds.pokemon.domain.GetPockemonDataUseCase
import com.ds.pokemon.domain.GetPokemonsUseCase
import com.ds.pokemon.domain.RestGetPokemonDataUseCase
import com.ds.pokemon.domain.RestGetPokemonsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single<GetPokemonsUseCase> { RestGetPokemonsUseCase(get()) }

    single<GetPockemonDataUseCase> { RestGetPokemonDataUseCase(get()) }
}
