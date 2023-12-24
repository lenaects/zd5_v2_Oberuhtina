package com.example.zd5_up_versia3.adapt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.R
import com.example.zd5_up_versia3.dataclass.Zakaz
import com.example.zd5_up_versia3.holder.ZakazViewHolder

class ZakazWorkerAdapter (
    private val zakazList: List<Zakaz>,
) : RecyclerView.Adapter<ZakazViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZakazViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_zakaz_worker, parent, false)


        return ZakazViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ZakazViewHolder, position: Int) {
        val currentzakaz = zakazList[position]
        holder.loginUser.text = currentzakaz.loginUser
        holder.titelMovie.text = currentzakaz.titelMovie
        holder.time.text = currentzakaz.time
        holder.data.text = currentzakaz.data
    }




    override fun getItemCount(): Int {
        return zakazList.size
    }

}