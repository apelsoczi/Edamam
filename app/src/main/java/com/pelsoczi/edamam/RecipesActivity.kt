package com.pelsoczi.edamam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pelsoczi.edamam.databinding.RecipeActivityBinding
import com.pelsoczi.edamam.ui.RecipesFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecipesActivity : AppCompatActivity() {

    private lateinit var binding: RecipeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RecipeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RecipesFragment())
                .commitNow()
        }
    }

}