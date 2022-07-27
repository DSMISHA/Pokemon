package com.ds.pokemon.ui.pokemon_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ds.pokemon.R
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PokemonListActivity : AppCompatActivity() {

    private val viewModel by viewModel<PokemonListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //todo add view binding

        setContentView(R.layout.activity_main)

        viewModel.pokemons.observe(this) {
            val t = 5
        }

        lifecycleScope.launch {
            viewModel.error.collect {
                val t = 5
//todo                toast()
            }
        }
    }

/*todo
    fun snackbar(view: View, message: String) {
        Snackbar.make(parentLayout, "This is main activity", Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
            .show()
    }
 */
}
