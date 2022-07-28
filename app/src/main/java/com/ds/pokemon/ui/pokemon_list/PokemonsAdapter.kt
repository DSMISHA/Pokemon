package com.ds.pokemon.ui.pokemon_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.ds.pokemon.R
import com.ds.pokemon.databinding.ItemPokemonBinding
import com.ds.pokemon.extension.capitalize
import com.ds.pokemon.extension.loadImage
import com.ds.pokemon.presentation.pokemon.Pokemon
import java.util.*


class PokemonsAdapter(
    private val pokemons: List<Pokemon>,
    private val onClick: (pokemon: Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPokemonBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(pokemons[position])
    }

    override fun getItemCount() = pokemons.size

    class ViewHolder(
        private val binding: ItemPokemonBinding,
        private val onClick: (pokemon: Pokemon) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val placeHolder = ResourcesCompat.getDrawable(
            binding.root.context.resources,
            R.drawable.ic_placeholder,
            null
        )

        fun onBind(pokemon: Pokemon) {
            binding.tvPokemonName.text = pokemon.name.capitalize()
            binding.card.setOnClickListener { onClick(pokemon) }
            binding.ivPokemon.loadImage(getImgUrl(pokemon.id), placeHolder, true)
        }

        private fun getImgUrl(pokemonId: String) = "$IMG_URL_PREFIX$pokemonId.png"
    }

    companion object {
        private const val IMG_URL_PREFIX =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/"
    }
}
