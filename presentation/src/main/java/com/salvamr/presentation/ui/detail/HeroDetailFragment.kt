package com.salvamr.presentation.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.salvamr.presentation.databinding.FragmentHeroDetailBinding
import kotlinx.coroutines.launch

class HeroDetailFragment : Fragment() {

    private var _binding: FragmentHeroDetailBinding? = null
    private val binding get() = _binding!!

    private val heroArg by navArgs<HeroDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = heroArg.heroDetailArguments

        binding.heroName.text = args.name
        binding.heroImage.load(args.thumbnail)
        binding.heroDescription.text = args.description
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}