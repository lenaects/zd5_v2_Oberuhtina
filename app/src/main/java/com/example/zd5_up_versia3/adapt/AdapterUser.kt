package com.example.zd5_up_versia3.adapt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.R
import com.example.zd5_up_versia3.dataclass.Users
import com.example.zd5_up_versia3.holder.UserViewHolder

class AdapterUser (private val userList: List<Users>, private val onDeleteClickListener: (Users) -> Unit,
                   private val onEditClickListener: (Users) -> Unit) :
    RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.login.text = currentUser.login
        holder.email.text = currentUser.email
        holder.password.text = currentUser.password
        // Обработчик события для кнопки изменения
        holder.editButton.setOnClickListener {
            onEditClickListener(currentUser)
        }
        // Обработчик события для кнопки удаления
        holder.deleteButton.setOnClickListener {
            onDeleteClickListener(currentUser)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}