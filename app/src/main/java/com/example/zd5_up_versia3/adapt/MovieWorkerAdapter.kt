package com.example.zd5_up_versia3.adapt

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.R
import com.example.zd5_up_versia3.dataclass.Movies
import com.example.zd5_up_versia3.holder.MovieWorkerViewHolder
import com.squareup.picasso.Picasso

class MovieWorkerAdapter (private val movieList: List<Movies>, private val onDeleteClickListener: (Movies) -> Unit,
                          private val onEditClickListener: (Movies) -> Unit) :
    RecyclerView.Adapter<MovieWorkerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieWorkerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_worker_movie, parent, false)
        return MovieWorkerViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MovieWorkerViewHolder, position: Int) {
        val currentMovie = movieList[position]
        Picasso.get().load(currentMovie.img).into(holder.img)
        holder.title.text = currentMovie.title
        holder.gener.text = currentMovie.gener
        holder.limitation.text = currentMovie.limitation
        // Обработчик события для кнопки изменения
        holder.editbut.setOnClickListener {
            onEditClickListener(currentMovie)
        }
        // Обработчик события для кнопки удаления
        holder.deletebut.setOnClickListener {
            onDeleteClickListener(currentMovie)
        }
    }
    override fun getItemCount(): Int {
        return movieList.size
    }
}