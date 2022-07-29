package com.ds.pokemon.domain

import com.ds.pokemon.BuildConfig
import com.ds.pokemon.driver.RestApi
import com.ds.pokemon.presentation.pokemon.Pokemon

class RestGetPokemonsUseCase(private val restApi: RestApi) : GetPokemonsUseCase {

    override suspend operator fun invoke() =
        restApi.getPokemons(hashMapOf("limit" to ITEMS_MAX_LIMIT)).body()?.results
            ?.mapNotNull {
                //doesn't take pokemon item if name or url is null or empty
                if (it.name.isNullOrEmpty() || it.url.isNullOrEmpty()) {
                    null
                } else {
                    Pokemon(id = getPokemonId(it.url), name = it.name, url = it.url)
                }
            } ?: listOf()

    /** Parses a pokemon id from url  */
    private fun getPokemonId(url: String) = url.removePrefix(URL_PREFIX).removeSuffix("/")

    companion object {
        //All list of pokemons will be loaded. Since the application uses search on the mobile side,
        // it was decided not to use pagination. In addition, not much data is returned.
        private const val ITEMS_MAX_LIMIT = 10000
        private const val URL_PREFIX = BuildConfig.BASE_URL + "pokemon/"
    }
}
