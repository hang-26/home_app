package com.example.homeapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homeapp.adapter.ActiveAdapter
import com.example.homeapp.adapter.ItemListNameInterface
import com.example.homeapp.adapter.StatusAdapter
import com.example.homeapp.data.StatusDataClass
import com.example.homeapp.databinding.FragmentPostedBinding
import com.example.pay.CompleteActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class PostedFragment : Fragment() {

    lateinit var binding: FragmentPostedBinding

    lateinit var myPostAdapter: ActiveAdapter
    var statusList = mutableListOf<StatusDataClass>()

    lateinit var dataBaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPostedBinding.inflate(layoutInflater, container, false)
        dataBaseReference = FirebaseDatabase.getInstance().reference
        getData()
        return binding.root
    }

    fun getData() {
        var auth = Firebase.auth
        var userIdPost = auth.currentUser?.uid
        Log.d(javaClass.simpleName, "getData: $userIdPost")
        var query = dataBaseReference.child("Post").orderByChild("userPostId").equalTo(userIdPost!!)


        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (postSnap in snapshot.children) {
                        val postId = postSnap.child("postId").value as String
                        val address = postSnap.child("address").value as String
                        val postName = postSnap.child("postName").value as String
                        var priceString = postSnap.child("price").value
                        var price = priceString.toString().toDouble()
                        val userPostId = postSnap.child("userPostId").value.toString()
                        val state = postSnap.child("state").value as String

                        Log.d(javaClass.simpleName, "onDataChange: $userPostId")
                        Log.d(javaClass.simpleName, "onDataChange: $address")

                        // Truy vaasn tên theo idUser
                        val userReference = dataBaseReference.child("Users")
                        var queryUsers = userReference.child(userPostId)
                        queryUsers.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var userNamePost = snapshot.child("userName").value as String
                                Log.d(javaClass.simpleName, "onDataChange: $userNamePost")
                                //Them du lieu
                                statusList.add(StatusDataClass(postId,postName, state,userNamePost, price ))
                                setRecyclerView()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    fun setRecyclerView() {
        val recyclerView = binding.recyclerView
        myPostAdapter = ActiveAdapter(statusList, object : ItemListNameInterface
        {
            override fun onClick(position: Int) {
                setOnClickListPosition(position)
            }

            override fun updateOnClick(position: Int) {
                TODO("Not yet implemented")
            }

        })
        val layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = myPostAdapter
        recyclerView.layoutManager = layoutManager

    }


    fun setOnClickListPosition(pos: Int) {
        var listPost = statusList[pos]
        var postId = listPost.postId
        var intent = Intent(context, CompleteActivity::class.java)
        intent.putExtra("id", postId)
        startActivity(intent)
    }


}