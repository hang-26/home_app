package com.example.work

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.homeapp.R
import com.example.homeapp.adapter.ItemListNameInterface
import com.example.homeapp.adapter.StatusAdapter
import com.example.homeapp.data.StatusDataClass
import com.example.homeapp.databinding.ActivityListCateBinding
import com.example.homeapp.databinding.FragmentListCateBinding
import com.example.homeapp.fragment.DetailPostFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class ListCateActivity : AppCompatActivity() {

    lateinit var binding: ActivityListCateBinding
    lateinit var database: DatabaseReference

    lateinit var statusAdapter: StatusAdapter
    var statusList = mutableListOf<StatusDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListCateBinding.inflate(layoutInflater)

//        val bundle = arguments
//        if (bundle != null) {
//            val title = bundle.getString("key")
//
//            database = FirebaseDatabase.getInstance().reference
//            readDataFromCategory(title!!)
//        }
        var intent2 = intent
        val title = intent2.getStringExtra("key")
        database = FirebaseDatabase.getInstance().reference
        readDataFromCategory(title!!)

        setContentView(R.layout.activity_list_cate)
    }

    fun readDataFromCategory(cate: String) {
        val query: Query = database.child("Post").orderByChild("cateId").equalTo(cate)

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
                        val userReference = database.child("Users")
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
            }

        })
    }

    fun setRecyclerView() {
        val recyclerView = binding.recyclerView
        statusAdapter = StatusAdapter(statusList, object : ItemListNameInterface
        {
            override fun onClick(position: Int) {
                setOnClickListPosition(position)
            }

            override fun updateOnClick(position: Int) {
                TODO("Not yet implemented")
            }

        })
        val layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = statusAdapter
        recyclerView.layoutManager = layoutManager

    }

    fun setOnClickListPosition(pos: Int) {
        var fragmentDetailPostragment = DetailPostFragment()
        var listPosition = statusList[pos]
        var posId = listPosition.postId
        Log.d(javaClass.simpleName, "setOnClickListPosition: $posId")
        var bundle: Bundle = Bundle()
        bundle.putString("id", posId)
        fragmentDetailPostragment.arguments = bundle
        sendDataToFragment(fragmentDetailPostragment)

    }

    fun sendDataToFragment(fragment: Fragment) {
        val fragmenManager = supportFragmentManager
        val  fragmentTransaction = fragmenManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.layoutFragment, fragment )
        fragmentTransaction?.commit()
    }

}