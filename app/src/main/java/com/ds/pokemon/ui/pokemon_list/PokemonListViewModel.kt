package com.ds.pokemon.ui.pokemon_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ds.pokemon.domain.Pokemon
import com.ds.pokemon.driver.RestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class PokemonListViewModel(private val restApi: RestApi) : ViewModel() {

    private val _pokemons = MutableLiveData<List<Pokemon>>()
    val pokemons: LiveData<List<Pokemon>> = _pokemons

    private val _error = MutableSharedFlow<Exceptions>()
    val error get() = _error.asSharedFlow()

/* todo
    private val context = Dispatchers.IO + CoroutineExceptionHandler { context, throwable ->
        viewModelScope.launch { _error.emit(Exceptions.POKEMONS_LIST_LOADING) }
    }
*/

    private fun start() {
        loadPokemons()
    }

    //todo error handler
    private fun loadPokemons() {
        viewModelScope.launch(Dispatchers.IO/*context*/) {
            _pokemons.postValue(
                restApi.getPokemons(hashMapOf("limit" to 10000)).body()?.results ?: listOf()
            )
        }
    }

    companion object {
        fun create(restApi: RestApi) = PokemonListViewModel(restApi).apply { start() }

        enum class Exceptions {
            POKEMONS_LIST_LOADING
        }
    }
}
