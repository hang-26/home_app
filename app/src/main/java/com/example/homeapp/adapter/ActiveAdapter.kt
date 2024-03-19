package com.example.homeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.homeapp.data.StatusDataClass
import com.example.homeapp.databinding.ItemActiveLayoutBinding
import com.example.homeapp.databinding.ItemStatusBinding

class ActiveAdapter(var list: MutableList<StatusDataClass>,
                    var onClick: ItemListNameInterface)
    :RecyclerView.Adapter<ActiveAdapter.ViewHolder>(){

    lateinit var binding: ItemActiveLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
        binding = ItemActiveLayoutBinding.inflate(view, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActiveAdapter.ViewHolder, position: Int) {
        var listPost = list[holder.adapterPosition]
        val bindingHolder = holder.binding
        bindingHolder.tvContent.text = listPost.postName
        bindingHolder.tvPrice.text = listPost.price.toString()
        bindingHolder.tvState.text = listPost.state
        bindingHolder.tvUserPost.text = listPost.namePost

        holder.itemView.setOnClickListener {
            onClick.onClick(holder.adapterPosition)
        }

    }

    override fun getItemCount(): Int {

return list.size
    }

    class ViewHolder( val binding: ItemActiveLayoutBinding):
        RecyclerView.ViewHolder(binding.root)

}