package com.example.zd5_up_versia3.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.R

class ZakazUzerViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    val titelMovie: TextView = itemView.findViewById(R.id.moviezakaz)
    val time : TextView = itemView.findViewById(R.id.timezakaz)
    val data : TextView = itemView.findViewById(R.id.datazakaz)
}