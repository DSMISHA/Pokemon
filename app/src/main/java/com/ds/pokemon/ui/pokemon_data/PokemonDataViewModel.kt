package com.ds.pokemon.ui.pokemon_data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ds.pokemon.domain.GetPokemonDataUseCase
import com.ds.pokemon.extension.asLiveData
import com.ds.pokemon.presentation.pokemon_data.PokemonData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PokemonDataViewModel(
    private val pokemonId: String,
    private val getPokemonDataUseCase: GetPokemonDataUseCase
) : ViewModel() {

    /** Pokemon data source for observing */
    private val _pokemonData = MutableLiveData<PokemonDataState>()
    val pokemonData = _pokemonData.asLiveData()

    /** Error loading event flow */
    private val _error = MutableSharedFlow<PokemonDataState.Error>()
    val error get() = _error.asSharedFlow()

    /** Coroutine context with Dispatcher and Error Handler */
    private val context = Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch { _error.emit(PokemonDataState.Error) }
    }

    /** This method starts pokemon data loading after ViewModel creation */
    private fun start() {
        loadPokemonData()
    }

    private fun loadPokemonData() {
        _pokemonData.value = PokemonDataState.Loading
        viewModelScope.launch(context) {
            val result = getPokemonDataUseCase(pokemonId)
            _pokemonData.postValue(PokemonDataState.Data(result))
        }
    }

    /** Three variants of pokemon data */
    sealed class PokemonDataState {
        object Loading : PokemonDataState()
        data class Data(val pokemonData: PokemonData) : PokemonDataState()
        object Error : PokemonDataState()
    }

    /** This method creates the ViewModel */
    companion object {
        fun create(pokemonId: String, getPokemonDataUseCase: GetPokemonDataUseCase) =
            PokemonDataViewModel(pokemonId, getPokemonDataUseCase).apply { start() }
    }
}
