package com.ds.pokemon.ui.pokemon_data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ds.pokemon.domain.GetPockemonDataUseCase
import com.ds.pokemon.extension.asLiveData
import com.ds.pokemon.presentation.pokemon_data.PokemonData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PokemonDataViewModel(
    private val pokemonId: String,
    private val getPockemonDataUseCase: GetPockemonDataUseCase
) : ViewModel() {

    private val _pokemonData = MutableLiveData<PokemonDataState>()
    val pokemonData = _pokemonData.asLiveData()

    private val _error = MutableSharedFlow<PokemonDataState.Error>()
    val error get() = _error.asSharedFlow()

    private val context = Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch { _error.emit(PokemonDataState.Error) }
    }

    private fun start() {
        loadPokemonData()
    }

    private fun loadPokemonData() {
        _pokemonData.value = PokemonDataState.Loading
        viewModelScope.launch(context) {
            val result = getPockemonDataUseCase(pokemonId)
            _pokemonData.postValue(PokemonDataState.Data(result))
        }
    }

    sealed class PokemonDataState {
        object Loading : PokemonDataState()
        data class Data(val pokemonData: PokemonData) : PokemonDataState()
        object Error : PokemonDataState()
    }

    companion object {
        fun create(pokemonId: String, getPockemonDataUseCase: GetPockemonDataUseCase) =
            PokemonDataViewModel(pokemonId, getPockemonDataUseCase).apply { start() }
    }
}
