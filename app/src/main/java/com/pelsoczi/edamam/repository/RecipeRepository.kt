package com.pelsoczi.edamam.repository

import com.google.gson.Gson
import com.pelsoczi.edamam.BuildConfig.*
import com.pelsoczi.edamam.api.RecipeSearchResponse
import com.pelsoczi.edamam.api.RecipesResponse
import com.pelsoczi.edamam.api.RecipesResponse.RecipesError
import com.pelsoczi.edamam.api.RecipesResponse.RecipesSuccess
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RecipeRepository @Inject constructor(
    private val client: OkHttpClient
) {

    /** Search the Recipes API and returns a [RecipesSuccess] or else [RecipesError] response */
    fun search(query: String): RecipesResponse {
        val url = "$EDAMAM_API/recipes/v2?type=public&app_id=$EDAMAM_ID&app_key=$EDAMAM_KEY" +
                "&q=$query" +
                "&imageSize=LARGE"
        val request = Request.Builder().url(url).build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException()
                val bodyJson = response.body?.string() ?: throw IOException()
                val recipeResponse = Gson().fromJson(bodyJson, RecipeSearchResponse::class.java)
                RecipesSuccess(recipeResponse.hits.toList())
            }
        } catch (e: Exception) {
            RecipesError
        }
    }

}
