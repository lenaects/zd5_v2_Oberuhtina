package com.example.zd5_up_versia3.adapt

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.dataclass.Movies
import com.example.zd5_up_versia3.holder.MovieUserViewHolder
import com.squareup.picasso.Picasso
import com.example.zd5_up_versia3.R

class MovieUserAdapter (
    private val onBuyClickListener: (Movies) -> Unit,
    private val onBookClickListener: (Movies) -> Unit
) : RecyclerView.Adapter<MovieUserViewHolder>() {

    private val originalMovieList = mutableListOf<Movies>()
    private val filteredMovieList = mutableListOf<Movies>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieUserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_user, parent, false)

        return MovieUserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieUserViewHolder, position: Int) {
        val currentMovie = filteredMovieList[position]

        Picasso.get().load(currentMovie.img).into(holder.img)

        holder.title.text = currentMovie.title
        holder.gener.text = currentMovie.gener
        holder.limitation.text = currentMovie.limitation
        holder.bron.setOnClickListener {
            onBuyClickListener(currentMovie)
        }

        holder.bron.setOnClickListener {
            onBookClickListener(currentMovie)
        }
    }

    override fun getItemCount(): Int {
        return filteredMovieList.size
    }

    fun setData(movieList: List<Movies>, gener: String) {
        originalMovieList.clear()
        originalMovieList.addAll(movieList)

        // Фильтруем туры по стране
        filteredMovieList.clear()
        filteredMovieList.addAll(originalMovieList.filter { it.gener == gener })

        notifyDataSetChanged()
    }
}