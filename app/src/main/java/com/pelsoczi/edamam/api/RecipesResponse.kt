package com.pelsoczi.edamam.api

import com.pelsoczi.edamam.vo.Hits

/** Allows fine tuning and representing Recipe API responses */
sealed class RecipesResponse {

    /** A response was received with a 200 code and interesting data */
    data class RecipesSuccess(
        val searchResponseRecipes: List<Hits>
    ) : RecipesResponse()

    /** Any other scenario not detailed in [RecipesResponse], an error which should be acted on */
    object RecipesError : RecipesResponse()

}
