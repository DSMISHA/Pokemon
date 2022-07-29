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

    private fun subscribeOnError() {
        lifecycleScope.launch {
            viewModel.error.collectLatest {
                showError()
                showLoading(false)
            }
        }
    }

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
            PokemonDataDialog.create(it.id, it.name)
                .show(supportFragmentManager, PokemonDataDialog.TAG)
        }
        binding.tvCount.text = pokemons.size.toString()
    }

    private fun showError() = binding.root.showErrorSnackBar()
}
