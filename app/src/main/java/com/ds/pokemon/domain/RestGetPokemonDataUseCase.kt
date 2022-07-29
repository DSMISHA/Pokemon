package com.ds.pokemon.domain

import com.ds.pokemon.domain.pokemonData.AbilityModel
import com.ds.pokemon.domain.pokemonData.OfficialArtWorkModel
import com.ds.pokemon.domain.pokemonData.OtherModel
import com.ds.pokemon.domain.pokemonData.SpritesModel
import com.ds.pokemon.driver.RestApi
import com.ds.pokemon.presentation.pokemon_data.*

class RestGetPokemonDataUseCase(private val restApi: RestApi) : GetPokemonDataUseCase {
    override suspend fun invoke(pokemonId: String) = restApi.getPokemonData(pokemonId).body()?.let {
        PokemonData(
            it.abilities.mapNotNull { ability -> ability.dto },
            it.sprites?.dto
        )
    } ?: throw IllegalArgumentException()

    //DTO
    private val AbilityModel.dto get() = abilityName?.name?.let { Ability(it) }
    private val SpritesModel.dto get() = Sprites(other?.dto)
    private val OtherModel.dto get() = Other(officialArtwork?.dto)
    private val OfficialArtWorkModel.dto get() = OfficialArtWork(frontDefault)
}
