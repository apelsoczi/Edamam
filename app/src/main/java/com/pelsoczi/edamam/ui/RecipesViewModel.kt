package com.pelsoczi.edamam.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pelsoczi.edamam.api.RecipesResponse
import com.pelsoczi.edamam.api.RecipesResponse.RecipesError
import com.pelsoczi.edamam.api.RecipesResponse.RecipesSuccess
import com.pelsoczi.edamam.repository.RecipeRepository
import com.pelsoczi.edamam.ui.RecipesViewState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/** Handles UI events from the User or System and produces [RecipesViewState] side effects via Flow emissions */
@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _viewState = MutableStateFlow<RecipesViewState>(Empty)
    val viewState: StateFlow<RecipesViewState> = _viewState

    /**
     * Search the [RecipeRepository] by keyword or phrase, and causes this viewModel's
     * viewState to emit a [RecipesSuccess] or [RecipesError]
     * @param query a non blank string to search for
     */
    fun searchRepository(query: String) {
        if (query.isBlank()) {
            _viewState.value = Error
            return
        }

        viewModelScope.launch(dispatcher) {
            _viewState.value = Loading
            val recipes: RecipesResponse = repository.search(query.trim())
            _viewState.value = when (recipes) {
                is RecipesSuccess -> Success(query, recipes.searchResponseRecipes)
                is RecipesError -> Error
            }
        }
    }

    /** Set this viewModel to its initial state */
    fun reset() {
        _viewState.value = Empty
    }

}