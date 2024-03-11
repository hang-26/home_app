package com.example.homeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.homeapp.data.ListNameData
import com.example.homeapp.databinding.ItemListNameBinding

class ItemListAdapter(
                      var list: MutableList<ListNameData>,
                      var itemOnClick: ItemListNameInterface) :
RecyclerView.Adapter<ItemListAdapter.ViewHolderList>(){

    lateinit var binding: ItemListNameBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderList {
        val view = LayoutInflater.from(parent.context)
        binding = ItemListNameBinding.inflate(view, parent, false)
        return ViewHolderList(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolderList, position: Int) {
        val nameList = list[holder.adapterPosition]
        val bindingList = holder.binding
        bindingList.tvListTitle.text = nameList.title
        bindingList.imvLabelList.setImageResource(nameList.avtar)

        holder.itemView.setOnClickListener {
            itemOnClick.onClick(holder.adapterPosition)
        }
    }

    class ViewHolderList( val binding: ItemListNameBinding) :
        RecyclerView.ViewHolder(binding.root)
}