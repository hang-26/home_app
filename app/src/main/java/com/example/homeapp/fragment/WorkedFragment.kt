package com.example.homeapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homeapp.R
import com.example.homeapp.adapter.ItemListNameInterface
import com.example.homeapp.adapter.StatusAdapter
import com.example.homeapp.data.StatusDataClass
import com.example.homeapp.databinding.FragmentWorkedBinding
import com.example.pay.CompleteActivity
import com.example.pay.PaymentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class WorkedFragment : Fragment() {

    lateinit var binding: FragmentWorkedBinding

    lateinit var databaseReference: DatabaseReference
    lateinit var auth: FirebaseAuth

    var statusList = mutableListOf<StatusDataClass>()
    lateinit var myWorkAdapter: StatusAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWorkedBinding.inflate(layoutInflater, container, false)
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = Firebase.auth
        getData()
        return binding.root
    }

    fun getData() {
        var current = auth.currentUser
        val userId = current?.uid
        Log.d(javaClass.simpleName, "getData: $userId")

        var query = databaseReference.child("Post").orderByChild("userWorkId").equalTo(userId!!)


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



                        // Truy vaasn tên theo idUser
                        val userReference = databaseReference.child("Users")
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
        myWorkAdapter = StatusAdapter(statusList, object : ItemListNameInterface
        {
            override fun onClick(position: Int) {
                setOnClickListPosition(position)
            }

            override fun updateOnClick(position: Int) {
                TODO("Not yet implemented")
            }

        })
        val layoutManager = GridLayoutManager(context,2)
        recyclerView.adapter = myWorkAdapter
        recyclerView.layoutManager = layoutManager

    }

    fun setOnClickListPosition(pos: Int) {
        var listPost = statusList[pos]
        var postId = listPost.postId
        var intent = Intent(context, PaymentActivity::class.java)
        intent.putExtra("id", postId)
        startActivity(intent)
        getData()
    }

}