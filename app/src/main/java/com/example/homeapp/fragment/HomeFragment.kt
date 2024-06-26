package com.example.homeapp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homeapp.R
import com.example.homeapp.adapter.ItemListAdapter
import com.example.homeapp.adapter.ItemListNameInterface
import com.example.homeapp.adapter.StatusAdapter
import com.example.homeapp.data.ListNameData
import com.example.homeapp.data.StatusDataClass
import com.example.homeapp.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    var nameList = mutableListOf<ListNameData>()
    lateinit var listAdapter: ItemListAdapter

    lateinit var viewAdapter: StatusAdapter
    var postList = mutableListOf<StatusDataClass>()

    private val startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK) {
            getDataPost()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        nameList.add(ListNameData(R.drawable.ic_cleaning, "Dọn dẹp"))
        nameList.add(ListNameData(R.drawable.ic_cook, "Nấu ăn"))
        nameList.add(ListNameData(R.drawable.tool, "Sửa chữa"))
        // Inflate the layout for this fragment
        setViewList()
        getDataPost()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Nơi để đặt mã xử lý khi Fragment trở lại trạng thái hoạt động
        Log.d("PreviousFragment", "Fragment resumed")
    }
    fun setViewList() {
        val recyclerViewList = binding.recyclerViewList

        listAdapter =  ItemListAdapter( nameList, object: ItemListNameInterface {
            override fun onClick(position: Int) {
                setOnClickList(position)
            }

            override fun updateOnClick(position: Int) {
                TODO("Not yet implemented")
            }
        })

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewList.adapter = listAdapter
        recyclerViewList.layoutManager = layoutManager
    }


    fun setOnClickList(position: Int) {
        val fragmentListCate = ListCateFragment()
        var listPosition = nameList[position]
        var name = listPosition.title
        val bundle = Bundle()
        bundle.putString("key", name)
        fragmentListCate.arguments = bundle
        Log.d(javaClass.simpleName, "setOnClickList: $name")
        sendDataToFragment(fragmentListCate)

//        var listPosition = nameList[position]
//        var name = listPosition.title
//        val intent = Intent(context, ListCateActivity::class.java)
//        intent.putExtra("key", name)
//        startActivityForResult.launch(intent)
    }

    fun sendDataToFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransacion = fragmentManager?.beginTransaction()

        fragmentTransacion?.replace(R.id.layoutFragment, fragment)
        fragmentTransacion?.addToBackStack(null)
        fragmentTransacion?.commit()
    }


    fun setPost() {
        var postRecyclerView = binding.recyclerView
        viewAdapter = StatusAdapter(postList, object : ItemListNameInterface {
            override fun onClick(position: Int) {
                setOnClickListPosition(position)
            }

            override fun updateOnClick(position: Int) {

            }

        })

//        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false )
        val layoutManager = GridLayoutManager(context, 2)
        postRecyclerView.adapter = viewAdapter
        postRecyclerView.layoutManager = layoutManager

    }




    fun getDataPost() {
        var dataReference: DatabaseReference
        dataReference = FirebaseDatabase.getInstance().reference
        var query = dataReference.child("Post")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList. clear()
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


//                         Truy vaasn tên theo idUser
                        val userReference = dataReference.child("Users")
                        var queryUsers = userReference.child(userPostId)
                        queryUsers.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var userNamePost = snapshot.child("userName").value as String
                                Log.d(javaClass.simpleName, "onDataChange: $userNamePost")
                                //Them du lieu
                                if (state == "Đang chờ") {
                                    postList.add(StatusDataClass(postId,postName, state,userNamePost, price ))
                                    setPost()
                                }

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

    fun setOnClickListPosition(pos: Int) {
        var fragmentDetailPostragment = DetailPostFragment()
        var listPosition = postList[pos]
        var posId = listPosition.postId
        Log.d(javaClass.simpleName, "setOnClickListPosition: $posId")
        var intent = Intent(context, fragmentDetailPostragment::class.java)
        intent.putExtra("id", posId)
        var bundle: Bundle = Bundle()
        bundle.putString("id", posId)
        fragmentDetailPostragment.arguments = bundle
        sendDataToFragment(fragmentDetailPostragment)

    }


}