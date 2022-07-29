package com.ds.pokemon.ui.pokemon_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ds.pokemon.domain.GetPokemonsUseCase
import com.ds.pokemon.extension.asLiveData
import com.ds.pokemon.presentation.pokemon.Pokemon
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class PokemonListViewModel(private val getPokemonsUseCase: GetPokemonsUseCase) : ViewModel() {

    private val _pokemons = MutableLiveData<PokemonListState>()
    val pokemons = _pokemons.asLiveData()

    private val _error = MutableSharedFlow<PokemonListState.Error>()
    val error get() = _error.asSharedFlow()

    private val allPokemons = MutableLiveData<List<Pokemon>>(listOf())

    private val filter = MutableStateFlow("")

    private val context = Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch { _error.emit(PokemonListState.Error) }
    }

    private fun start() {
        loadPokemons()
        subscribeOnSearch()
    }

    private fun loadPokemons() {
        _pokemons.value = PokemonListState.Loading
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
            filter.debounce(TIME_DEBOUNCE).collectLatest { query ->
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
        private const val TIME_DEBOUNCE = 500L

        fun create(getPokemonsUseCase: GetPokemonsUseCase) =
            PokemonListViewModel(getPokemonsUseCase).apply { start() }
    }

    private fun List<Pokemon>.filterByName(query: String) = filter { it.name.startsWith(query) }
}
