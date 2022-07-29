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
import com.ds.pokemon.presentation.pokemon_data.Sprites
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/** This dialog screen shows extended information about a pokemon */

class PokemonDataDialog @Deprecated("Use the create method") constructor() : DialogFragment() {

    /** pokemon's id getter  */
    private val pokemonId
        get() = requireArguments().getString(KEY_POKEMON_ID)
            ?: IllegalArgumentException("Pokemon's id is null")

    /** pokemon's name getter  */
    private val pokemonName
        get() = requireArguments().getString(KEY_POKEMON_NAME)
            ?: getString(R.string.pokemon_empty_name)

    private val viewModel by viewModel<PokemonDataViewModel> { parametersOf(pokemonId) }

    private lateinit var binding: FragmentPokemonDataBinding

    /**
     * This theme makes the dialog background transparent
     * also stretches it to full screen and gives animation for it appearing
     *  */
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

    /**
     * This method subscribes collector on error events which can arise during data loading process
     * */
    private fun subscribeOnError() {
        lifecycleScope.launch {
            viewModel.error.collectLatest {
                showError()
                showLoading(false)
            }
        }
    }

    /** This method subscribes on liveData source with loaded pokemon data */
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
        showPokemonImage(data.sprites)
        setPokemonAbilities(data.abilities)
    }

    private fun showPokemonImage(sprites: Sprites?) {
        val image = sprites?.other?.officialArtwork?.frontDefault ?: return

        //show linear activity indicator
        binding.progressImg.isVisible = true

        //load image from url
        binding.ivImg.loadImage(
            image,
            ContextCompat.getDrawable(requireContext(), R.drawable.big_placeholder)
        ) {
            //hide linear activity indicator on loading finished
            binding.progressImg.isVisible = false
        }
    }

    private fun setPokemonAbilities(abilities: List<Ability>) {
        binding.tvAbilities.text = getAbilitiesString(abilities)
    }

    /** Creates a row from abilities separated by comma and sets "Abilities" word bold */
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

        /** This method creates the Dialog */
        @Suppress("deprecation")
        fun create(pokemonId: String, name: String) = PokemonDataDialog().apply {
            arguments = bundleOf(KEY_POKEMON_ID to pokemonId, KEY_POKEMON_NAME to name)
        }
    }
}
