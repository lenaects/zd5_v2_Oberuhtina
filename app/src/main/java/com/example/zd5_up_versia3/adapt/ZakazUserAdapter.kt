package com.example.zd5_up_versia3.adapt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.R
import com.example.zd5_up_versia3.dataclass.Movies
import com.example.zd5_up_versia3.dataclass.Zakaz
import com.example.zd5_up_versia3.holder.MovieUserViewHolder
import com.example.zd5_up_versia3.holder.ZakazUzerViewHolder
import com.example.zd5_up_versia3.holder.ZakazViewHolder
import com.squareup.picasso.Picasso

class ZakazUserAdapter (
    private var zakazList: List<Zakaz>,
    private val currentUserLogin: String?
) : RecyclerView.Adapter<ZakazUzerViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZakazUzerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_zakaz_uzer, parent, false)

        return ZakazUzerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ZakazUzerViewHolder, position: Int) {
        val currentzakaz = zakazList[position]
        holder.titelMovie.text = currentzakaz.titelMovie
        holder.time.text = currentzakaz.time
        holder.data.text = currentzakaz.data
    }

    override fun getItemCount(): Int {
        return zakazList.size
    }

    fun setData(zakazList: List<Zakaz>) {
        this.zakazList = zakazList
        notifyDataSetChanged()
    }
}