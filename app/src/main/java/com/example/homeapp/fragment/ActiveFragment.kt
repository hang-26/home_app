package com.example.homeapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homeapp.R
import com.example.homeapp.adapter.ItemListNameInterface
import com.example.homeapp.adapter.StatusAdapter
import com.example.homeapp.data.StatusDataClass
import com.example.homeapp.databinding.FragmentActiveBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class ActiveFragment : Fragment() {

    lateinit var bindding: FragmentActiveBinding

    lateinit var myPostAdapter: StatusAdapter
    var statusList = mutableListOf<StatusDataClass>()

    lateinit var dataBaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindding = FragmentActiveBinding.inflate(layoutInflater, container, false)
        dataBaseReference = FirebaseDatabase.getInstance().reference

        getData()
        return bindding.root
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
        val recyclerView = bindding.recyclerView
        myPostAdapter = StatusAdapter(statusList, object : ItemListNameInterface
        {
            override fun onClick(position: Int) {
//                setOnClickListPosition(position)
            }

        })
        val layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = myPostAdapter
        recyclerView.layoutManager = layoutManager

    }

}