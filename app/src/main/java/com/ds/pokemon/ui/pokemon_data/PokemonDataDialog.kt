package com.ds.pokemon.ui.pokemon_data

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.ds.pokemon.R
import com.ds.pokemon.databinding.FragmentPokemonDataBinding
import com.ds.pokemon.extension.capitalize
import com.ds.pokemon.extension.loadImage
import com.ds.pokemon.extension.showErrorSnackBar
import com.ds.pokemon.presentation.pokemon_data.Ability
import com.ds.pokemon.presentation.pokemon_data.PokemonData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PokemonDataDialog : DialogFragment() {

    private val pokemonId
        get() = requireArguments().getString(KEY_POKEMON_ID)
            ?: IllegalArgumentException("Pokemon's id is null")

    private val pokemonName
        get() = requireArguments().getString(KEY_POKEMON_NAME)
            ?: getString(R.string.pokemon_empty_name)

    private val viewModel by viewModel<PokemonDataViewModel> { parametersOf(pokemonId) }

    private lateinit var binding: FragmentPokemonDataBinding

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPokemonDataBinding.inflate(inflater, container, false)
        .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ctaClose.setOnClickListener { dismiss() }

        setPokemonName()
        subscribeOnError()
        subscribeOnPokemonData()
    }

    private fun setPokemonName() {
        binding.tvName.text = pokemonName
    }

    private fun subscribeOnError() {
        lifecycleScope.launch {
            viewModel.error.collectLatest {
                showError()
                showLoading(false)
            }
        }
    }

    private fun subscribeOnPokemonData() {
        viewModel.pokemonData.observe(this) {
            when (it) {
                is PokemonDataViewModel.PokemonDataState.Loading -> showLoading(true)
                is PokemonDataViewModel.PokemonDataState.Data -> {
                    showData(it.pokemonData)
                    showLoading(false)
                }
                else -> {
                    //no op
                }
            }
        }
    }

    private fun showData(data: PokemonData) {
        binding.progressImg.isVisible = true
        val image = data.sprites?.other?.officialArtwork?.frontDefault ?: return
        binding.ivImg.loadImage(
            image,
            ContextCompat.getDrawable(requireContext(), R.drawable.big_placeholder)
        ) {
            binding.progressImg.isVisible = false
        }

        binding.tvAbilities.text = getAbilitiesString(data.abilities)
    }

    private fun getAbilitiesString(abilities: List<Ability>): SpannableString {
        val abilitiesString = getString(R.string.pokemon_abilities)
        return abilities.map { it.name.capitalize() }
            .toString().replace("[", "").replace("]", "")
            .let { "$abilitiesString: $it" }
            .let {
                val ss = SpannableString(it)
                val boldSpan = StyleSpan(Typeface.BOLD)
                ss.setSpan(boldSpan, 0, abilitiesString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                ss
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progress.isVisible = isLoading
    }

    private fun showError() = binding.root.showErrorSnackBar()

    companion object {
        val TAG = PokemonDataDialog::class.simpleName
        private const val KEY_POKEMON_ID = "KEY_POKEMON_ID"
        private const val KEY_POKEMON_NAME = "KEY_POKEMON_NAME"

        fun create(pokemonId: String, name: String) = PokemonDataDialog().apply {
            arguments = bundleOf(KEY_POKEMON_ID to pokemonId, KEY_POKEMON_NAME to name)
        }
    }
}
