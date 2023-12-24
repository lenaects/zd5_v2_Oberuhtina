package com.example.zd5_up_versia3.adapt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.R
import com.example.zd5_up_versia3.dataclass.GroupMovie
import com.example.zd5_up_versia3.holder.GroupGenerViewHolder

class GroupGenerAdapter (
    private val groupCountryList: List<GroupMovie>,
    private val onItemClick: (GroupMovie) -> Unit
) : RecyclerView.Adapter<GroupGenerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupGenerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group_gener, parent, false)


        return GroupGenerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GroupGenerViewHolder, position: Int) {
        val currentGroup = groupCountryList[position]

        holder.gener.text = currentGroup.gener

        // Добавляем обработчик нажатия на элемент списка
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentGroup)
        }
    }


    override fun getItemCount(): Int {
        return groupCountryList.size
    }

}