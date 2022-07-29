package com.ds.pokemon.ioc.koin

import com.ds.pokemon.ui.pokemon_data.PokemonDataViewModel
import com.ds.pokemon.ui.pokemon_list.PokemonListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { PokemonListViewModel.create(get()) }

    viewModel { (pokemonId: String) -> PokemonDataViewModel.create(pokemonId, get()) }
}
