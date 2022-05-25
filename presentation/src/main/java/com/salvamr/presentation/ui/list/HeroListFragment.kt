package com.salvamr.presentation.ui.list

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.salvamr.presentation.R
import com.salvamr.presentation.databinding.FragmentHeroListBinding
import dagger.hilt.android.AndroidEntryPoint
import errors.Error
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HeroListFragment : Fragment() {

    private var _binding: FragmentHeroListBinding? = null
    private val binding get() = _binding!!

    private val heroListViewModel: HeroListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                heroListViewModel.heroList
                    .collectLatest {
                        when (it) {
                            is HeroListFragmentState.Error -> {
                                val message = when (it.throwable) {
                                    is Error.HeroNotFound -> {
                                        getString(R.string.hero_not_found)
                                    }
                                    else -> it.throwable.message ?: ""
                                }
                                Snackbar.make(
                                    view,
                                    message,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                hideProgressBar()
                            }
                            is HeroListFragmentState.HeroesRetrieved -> {
                                (binding.list.adapter as HeroListRecyclerViewAdapter).add(it.values)
                                hideProgressBar()
                            }
                            HeroListFragmentState.Loading -> showProgressBar()

                        }
                    }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroListBinding.inflate(inflater, container, false)

        binding.list.adapter = HeroListRecyclerViewAdapter { heroId ->
            lifecycleScope.launch {
                heroListViewModel.getHeroById(heroId)?.let { heroSelected ->
                    findNavController().navigate(
                        HeroListFragmentDirections.actionHeroListFragmentToHeroDetailFragment(
                            heroSelected
                        )
                    )
                }
            }
        }

        binding.list.addOnScrollListener(HeroListRecyclerViewScrollListener(
            binding.list.layoutManager as LinearLayoutManager,
            transform = {
                heroListViewModel.incrementPage()
                heroListViewModel.getHeroes()
            }
        ))

        return binding.root
    }

    private fun showProgressBar() {
        binding.progressBar.animate()
            .alpha(1f)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.progressBar.isVisible = true
                }
            })

    }

    private fun hideProgressBar() {
        binding.progressBar.animate()
            .alpha(0f)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.progressBar.isVisible = false
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}