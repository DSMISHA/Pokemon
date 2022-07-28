package com.ds.pokemon.ui.pokemon_list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ds.pokemon.R
import com.ds.pokemon.databinding.ActivityPokemonsListBinding
import com.ds.pokemon.extension.setEnabling
import com.ds.pokemon.presentation.pokemon.Pokemon
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class PokemonListActivity : AppCompatActivity() {

    private val viewModel by viewModel<PokemonListViewModel>()
    private lateinit var binding: ActivityPokemonsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonsListBinding.inflate(layoutInflater)
            .also { setContentView(it.root) }

        subscribeOnPokemonsData()
        initSearchView()
    }

    private fun subscribeOnPokemonsData() {
        viewModel.pokemons.observe(this) {
            when (it) {
                is PokemonListViewModel.PokemonListState.Loading -> showLoading(true)
                is PokemonListViewModel.PokemonListState.Data -> {
                    showLoading(false)
                    showPokemons(it.pokemons)
                }
                else -> showError()
            }
        }
    }

    private fun initSearchView() {
        binding.search.maxWidth = Int.MAX_VALUE
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) viewModel.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) viewModel.filter(newText)
                return true
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progress.isVisible = isLoading
        binding.progress.setEnabling(!isLoading)
    }

    private fun showPokemons(pokemons: List<Pokemon>) {
        binding.rvPokemons.layoutManager = LinearLayoutManager(this)
        binding.rvPokemons.adapter = PokemonsAdapter(pokemons) {
            //todo show a pokemon's data
        }
        binding.tvCount.text = pokemons.size.toString()
    }

    private fun showError() = snackbar(binding.root)

    private fun snackbar(view: View) {
        Snackbar.make(view, getString(R.string.error_loading_text), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.error_loading_btn_title)) { }
            .setActionTextColor(
                ResourcesCompat.getColor(resources, android.R.color.holo_red_light, null)
            ).show()
    }
}
