package com.pelsoczi.edamam.ui

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.pelsoczi.edamam.R
import com.pelsoczi.edamam.databinding.RecipeItemBinding
import com.pelsoczi.edamam.vo.Hits


class RecipesAdapter : RecyclerView.Adapter<RecipeViewHolder>() {

    /** Property access syntax to set recipe-hits to be displayed in [RecipesFragment] */
    var data: List<Hits> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecipeViewHolder(
            RecipeItemBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}

class RecipeViewHolder(val binding: RecipeItemBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val RECIPE_VIEW_TYPE = 100
    }

    fun bind(hits: Hits) {
        val glideRequestListener = object : RequestListener<Bitmap> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                hideLabels()
                return false
            }

            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                if (resource == null) {
                    hideLabels()
                } else {
                    Palette.from(resource).generate {
                        val black = itemView.resources.getColor(android.R.color.black)
                        val white = itemView.resources.getColor(android.R.color.white)
                        val backgroundColor = it?.darkMutedSwatch ?: it?.mutedSwatch ?: it?.lightMutedSwatch
                        binding.name.setBackgroundColor(backgroundColor?.rgb ?: black)
                        binding.name.setTextColor(backgroundColor?.bodyTextColor ?: white)
                        binding.source.setTextColor(backgroundColor?.bodyTextColor ?: white)
                        binding.source.setBackgroundColor(backgroundColor?.rgb ?: black)
                        binding.hashtagsBackground.setBackgroundColor(backgroundColor?.rgb ?: R.color.secondaryDarkColorOpaque)
                        binding.hashtags.setTextColor(backgroundColor?.titleTextColor ?: R.color.secondaryLightColor)
                    }
                }
                return false
            }
        }
        hits.recipe?.images?.large?.let {
            Glide.with(itemView.context)
                .asBitmap()
                .load(it.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .listener(glideRequestListener)
                .into(binding.image)
        }
        hideLabels()

        binding.name.text = hits.recipe?.label ?: ""
        binding.source.text = hits.recipe?.source ?: ""

        var hashtags = ""
        hits.recipe?.run {
            val mealtags = cuisineType + mealType + dietLabels + dishType
            val tags = mealtags.shuffled() + healthLabels.shuffled().take(10)
            tags.map { it.toLowerCase().replace(Regex("[^a-z]"), "") }
                .map { "#$it" }
                .forEach { hashtags = "$hashtags$it " }
        }
        binding.hashtags.text = hashtags.trim()
    }

    private fun hideLabels() {
        val backgroundColor = itemView.resources.getColor(android.R.color.transparent)
        val textColor = itemView.resources.getColor(android.R.color.transparent)
        binding.name.setBackgroundColor(backgroundColor)
        binding.name.setTextColor(textColor)
        binding.source.setTextColor(textColor)
        binding.source.setBackgroundColor(backgroundColor)
        binding.hashtagsBackground.setBackgroundColor(backgroundColor)
        binding.hashtags.setTextColor(textColor)
    }

}