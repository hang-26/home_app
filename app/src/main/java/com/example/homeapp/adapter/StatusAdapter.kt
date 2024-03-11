package com.example.homeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.homeapp.data.StatusDataClass
import com.example.homeapp.databinding.ItemListNameBinding
import com.example.homeapp.databinding.ItemStatusBinding

class StatusAdapter(
                    var list: MutableList<StatusDataClass>,
                    var onClick: ItemListNameInterface)
    : RecyclerView.Adapter<StatusAdapter.ViewHolderStatus> ()
{
    lateinit var binding: ItemStatusBinding



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderStatus {
        val view = LayoutInflater.from(parent.context)
        binding = ItemStatusBinding.inflate(view, parent, false)
        return ViewHolderStatus(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolderStatus, position: Int) {
        var listPost = list[holder.adapterPosition]
        val bindingHolder = holder.binding
        bindingHolder.tvUserPost.text = listPost.namePost
        bindingHolder.tvContent.text = listPost.postName
        bindingHolder.tvState.text = listPost.state
        bindingHolder.tvPrice.text = listPost.price.toString()

        holder.itemView.setOnClickListener {
            onClick.onClick(holder.adapterPosition)
        }
    }

    class ViewHolderStatus( val binding: ItemStatusBinding):
        RecyclerView.ViewHolder(binding.root)
}