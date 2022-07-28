package com.ds.pokemon.domain

import com.ds.pokemon.BuildConfig
import com.ds.pokemon.driver.RestApi
import com.ds.pokemon.presentation.pokemon.Pokemon

class RestGetPokemonsUseCase(private val restApi: RestApi) : GetPokemonsUseCase {

    override suspend operator fun invoke() =
        restApi.getPokemons(hashMapOf("limit" to ITEMS_MAX_LIMIT)).body()?.results
            ?.mapNotNull {
                if (it.name.isNullOrEmpty() || it.url.isNullOrEmpty()) {
                    null
                } else {
                    Pokemon(id = getPokemonId(it.url), name = it.name, url = it.url)
                }
            } ?: listOf()

    private fun getPokemonId(url: String) = url.removePrefix(URL_PREFIX).removeSuffix("/")

    companion object {
        private const val ITEMS_MAX_LIMIT = 10000
        private const val URL_PREFIX = BuildConfig.BASE_URL + "pokemon/"
    }
}
