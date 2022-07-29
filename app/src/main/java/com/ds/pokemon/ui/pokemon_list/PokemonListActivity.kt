package com.ds.pokemon.ui.pokemon_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ds.pokemon.databinding.ActivityPokemonsListBinding
import com.ds.pokemon.extension.setEnabling
import com.ds.pokemon.extension.showErrorSnackBar
import com.ds.pokemon.presentation.pokemon.Pokemon
import com.ds.pokemon.ui.pokemon_data.PokemonDataDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/** This is Main and single activity of the app that contains list of pokemons */
class PokemonListActivity : AppCompatActivity() {

    private val viewModel by viewModel<PokemonListViewModel>()
    private lateinit var binding: ActivityPokemonsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonsListBinding.inflate(layoutInflater)
            .also { setContentView(it.root) }

        subscribeOnError()
        subscribeOnPokemonsData()
        initSearchView()
    }

    /**
     * Subscribes on errors which can appear during loading data process for showing them to an user
     * */
    private fun subscribeOnError() {
        lifecycleScope.launch {
            viewModel.error.collectLatest {
                showError()
                showLoading(false)
            }
        }
    }

    /** Subscribes on pokemon list data */
    private fun subscribeOnPokemonsData() {
        viewModel.pokemons.observe(this) {
            when (it) {
                is PokemonListViewModel.PokemonListState.Loading -> showLoading(true)
                is PokemonListViewModel.PokemonListState.Data -> {
                    showPokemons(it.pokemons)
                    showLoading(false)
                }
                else -> {
                    //no op
                }
            }
        }
    }

    /** Initializes the search view at bottom of a screen */
    private fun initSearchView() {
        binding.search.maxWidth = Int.MAX_VALUE //expands the searchView on max width of a screen
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) viewModel.filter(query)
                if (!binding.search.isIconified) binding.search.clearFocus()
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

    /** Initializes pokemon adapter and shows list of pokemons */
    private fun showPokemons(pokemons: List<Pokemon>) {
        binding.rvPokemons.layoutManager = LinearLayoutManager(this)
        binding.rvPokemons.adapter = PokemonsAdapter(pokemons) {
            PokemonDataDialog.create(it.id, it.name)
                .show(supportFragmentManager, PokemonDataDialog.TAG)
        }

        //shows pokemon count
        binding.tvCount.text = pokemons.size.toString()
    }

    private fun showError() = binding.root.showErrorSnackBar()
}
