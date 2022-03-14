package com.pelsoczi.edamam.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pelsoczi.edamam.databinding.RecipeFragmentBinding
import com.pelsoczi.edamam.ui.RecipesViewState.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private val viewModel: RecipesViewModel by viewModels()

    private var _binding: RecipeFragmentBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: RecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.setOnClickListener {
            hideSoftKeyboard()
            viewModel.searchRepository(binding.searchBar.text.toString())
        }

        binding.resetButton.setOnClickListener {
            viewModel.reset()
        }

        adapter = RecipesAdapter()
        binding.recipesRecyclerView.adapter = adapter
        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewModel.viewState.collect {
                setViewState(it)
            }
        }
    }

    private fun setViewState(state: RecipesViewState) {
        when (state) {
            is Empty ->     VISIBLE
            is Error ->     GONE
            is Loading ->   GONE
            is Success ->   GONE
        }.let {
            binding.searchBar.visibility = it
            binding.searchBar.text?.clear()
        }

        when (state) {
            is Empty ->     VISIBLE
            is Error ->     GONE
            is Loading ->   GONE
            is Success ->   GONE
        }.let { binding.searchButton.visibility = it }

        when (state) {
            is Empty ->     GONE
            is Error ->     GONE
            is Loading ->   VISIBLE
            is Success ->   GONE
        }.let { binding.loading.visibility = it }

        when (state) {
            is Empty ->     emptyList()
            is Error ->     emptyList()
            is Loading ->   emptyList()
            is Success ->   state.recipes
        }.let { adapter.data = it }

        when (state) {
            is Empty ->     ""
            is Error ->     "ERROR: TRY AGAIN"
            is Loading ->   ""
            is Success ->   "SEARCH AGAIN !"
        }.let {
            binding.resetButton.isVisible = it.isNotEmpty()
            binding.resetButton.text = it
        }
    }

    private fun hideSoftKeyboard() {
        val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}