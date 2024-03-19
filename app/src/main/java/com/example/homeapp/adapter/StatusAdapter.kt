package com.example.homeapp.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.homeapp.R
import com.example.homeapp.data.StatusDataClass
import com.example.homeapp.databinding.ItemListNameBinding
import com.example.homeapp.databinding.ItemStatusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class StatusAdapter(
                    var list: MutableList<StatusDataClass>,
                    var onClick: ItemListNameInterface)
    : RecyclerView.Adapter<StatusAdapter.ViewHolderStatus> ()
{
    lateinit var binding: ItemStatusBinding

    lateinit var auth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference

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

        if (listPost.state == "Đang chờ") {
            bindingHolder.tvState.setBackgroundColor(Color.GRAY)
        } else if( listPost.state == "Đã được nhận") {
            bindingHolder.tvState.setBackgroundResource(R.color.coffee)
        }
        else if(listPost.state == "Đã hoàn thành") {
            bindingHolder.tvState.setBackgroundResource(R.color.custom_color_content)
        } else {
            bindingHolder.tvState.setBackgroundResource(R.color.custom_color_secondary)
        }


        holder.itemView.setOnClickListener {
            onClick.onClick(holder.adapterPosition)
        }
    }

    class ViewHolderStatus( val binding: ItemStatusBinding):
        RecyclerView.ViewHolder(binding.root)



}