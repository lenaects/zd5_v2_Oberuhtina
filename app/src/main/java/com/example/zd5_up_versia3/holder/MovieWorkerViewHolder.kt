package com.example.zd5_up_versia3.holder

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.R

class MovieWorkerViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    val img: ImageView = itemView.findViewById(R.id.imgText)
    val title: TextView = itemView.findViewById(R.id.titleText)
    val gener: TextView = itemView.findViewById(R.id.generText)
    val limitation: TextView = itemView.findViewById(R.id.limitationText)
    val editbut: ImageButton = itemView.findViewById(R.id.editMovie)
    val deletebut: ImageButton = itemView.findViewById(R.id.deletMovie)


}