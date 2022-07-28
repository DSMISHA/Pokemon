package com.ds.pokemon.ui.pokemon_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ds.pokemon.domain.GetPokemonsUseCase
import com.ds.pokemon.presentation.pokemon.Pokemon
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


class PokemonListViewModel(private val getPokemonsUseCase: GetPokemonsUseCase) : ViewModel() {

    private val _pokemons = MutableLiveData<PokemonListState>()
    val pokemons: LiveData<PokemonListState> = _pokemons

    private val allPokemons = MutableLiveData<List<Pokemon>>(listOf())

    private val filter = MutableStateFlow("")

    private val context = Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch { _pokemons.postValue(PokemonListState.Error) }
    }

    private fun start() {
        loadPokemons()
        subscribeOnSearch()
    }

    private fun loadPokemons() {
        _pokemons.postValue(PokemonListState.Loading)
        viewModelScope.launch(context) {
            val result = getPokemonsUseCase().sortedBy { it.name }
            allPokemons.postValue(result)
            val query = filter.value
            val state = PokemonListState.Data(result.filterByName(query))
            _pokemons.postValue(state)
        }
    }

    fun filter(str: String) {
        viewModelScope.launch {
            filter.emit(str.lowercase())
        }
    }

    @OptIn(FlowPreview::class)
    private fun subscribeOnSearch() {
        viewModelScope.launch {
            filter.debounce(500).collectLatest { query ->
                val filteredPokemons = allPokemons.value?.filterByName(query) ?: listOf()
                _pokemons.postValue(PokemonListState.Data(filteredPokemons))
            }
        }
    }

    sealed class PokemonListState {
        object Loading : PokemonListState()
        data class Data(val pokemons: List<Pokemon>) : PokemonListState()
        object Error : PokemonListState()
    }

    companion object {
        fun create(getPokemonsUseCase: GetPokemonsUseCase) =
            PokemonListViewModel(getPokemonsUseCase).apply { start() }
    }

    private fun List<Pokemon>.filterByName(query: String) = filter { it.name.startsWith(query) }
}
