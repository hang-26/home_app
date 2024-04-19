package com.example.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homeapp.R
import com.example.homeapp.adapter.ActiveAdapter
import com.example.homeapp.adapter.ItemListNameInterface
import com.example.homeapp.data.StatusDataClass
import com.example.homeapp.databinding.ActivityUserAccounBinding
import com.example.pay.CompleteActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserAccounActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserAccounBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var myPostAdapter: ActiveAdapter
    var statusList = mutableListOf<StatusDataClass>()
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserAccounBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        var intentAccount = intent
        userId = intentAccount.getStringExtra("userPostId")
        Log.d(javaClass.simpleName, "onCreate: $userId")
        setViewActivity()
        getData()

    }

    fun setViewActivity() {
        var userDataReference = databaseReference.child("Users")
        var query = userDataReference.child(userId!!)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var name = snapshot.child("userName").value as String
                    var numberPhone = snapshot.child("numberPhone").value as String
                    var rate = snapshot.child("rate").value
                    var address = snapshot.child("userAddress").value as String
                    Log.d(javaClass.simpleName, "onDataChange: $name")
                    binding.tvAddress.text = address
//                    binding.tvPhoneNumber.text = numberPhone

                    binding.tvRate.text = "Đánh giá: $rate"
                    binding.tvUserName.text = name

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getData() {
        var dataPost = databaseReference.child("Post")
        var query = dataPost.orderByChild("userPostId").equalTo(userId)


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
        myPostAdapter = ActiveAdapter(statusList, object : ItemListNameInterface
        {
            override fun onClick(position: Int) {
                setOnClickListPosition(position)
            }

            override fun updateOnClick(position: Int) {
                TODO("Not yet implemented")
            }

        })
        val layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = myPostAdapter
        recyclerView.layoutManager = layoutManager
    }

    fun setOnClickListPosition(pos: Int) {
        var listPost = statusList[pos]
        var postId = listPost.postId
        var intent = Intent(this, CompleteActivity::class.java)
        intent.putExtra("id", postId)
        startActivity(intent)
    }


}