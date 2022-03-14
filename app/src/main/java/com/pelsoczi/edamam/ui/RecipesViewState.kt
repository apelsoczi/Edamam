package com.pelsoczi.edamam.ui

import com.pelsoczi.edamam.vo.Hits


/** Represents possible UI states for [RecipesFragment] */
sealed class RecipesViewState {

    /** An idle or initialized state */
    object Empty : RecipesViewState()

    /** Searching for recipes, should disallow any actions on the UI */
    object Loading : RecipesViewState()

    /** Display recipes to the user for a query string */
    data class Success(
        val query: String,
        val recipes: List<Hits>
    ) : RecipesViewState()

    /** An error occurred requiring user input */
    object Error : RecipesViewState()

}