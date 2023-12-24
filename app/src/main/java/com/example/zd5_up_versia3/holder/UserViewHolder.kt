package com.example.zd5_up_versia3.holder

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.R

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val login: TextView = itemView.findViewById(R.id.textlogin)
    val email: TextView = itemView.findViewById(R.id.textemail)
    val password: TextView = itemView.findViewById(R.id.textpassword)
    val editButton: ImageButton = itemView.findViewById(R.id.editbutton)
    val deleteButton: ImageButton = itemView.findViewById(R.id.deletbuttoon)
}