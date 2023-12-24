package com.example.zd5_up_versia3.api

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.R
import com.squareup.picasso.Picasso

class FillScreen(private val context: Context, private val list: ArrayList<Movie>) : RecyclerView.Adapter<FillScreen.MyVH>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        val root = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return MyVH(root)
    }
    fun updateList(newList: List<Movie>) {
        list.clear()
        list.addAll(newList)
    }
    inner class MyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imageView)
        val title: TextView = itemView.findViewById(R.id.titleTextView)
        val gener: TextView = itemView.findViewById(R.id.genreTextView)
        //val limitation: TextView = itemView.findViewById(R.id.ratedTextView)
        val time: TextView = itemView.findViewById(R.id.runtimeTextView)
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        Picasso.get().load(list[position].img).into(holder.img)
        //holder.imageView.setImageResource(list[position].img)
        holder.title.setText(list[position].title)
        holder.gener.setText(list[position].gener)
        //holder.limitation.setText(list[position].limitation)
        holder.time.setText(list[position].runtime)
    }

    override fun getItemCount(): Int{
        return list.size
    }
}


