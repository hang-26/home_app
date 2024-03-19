package com.example.account

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homeapp.R
import com.example.homeapp.adapter.ActiveAdapter
import com.example.homeapp.adapter.ItemListNameInterface
import com.example.homeapp.data.StatusDataClass
import com.example.homeapp.databinding.ActivityInfoAccountBinding
import com.example.homeapp.databinding.UserEditBinding
import com.example.pay.CompleteActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class InfoAccountActivity : AppCompatActivity() {

    lateinit var binding:ActivityInfoAccountBinding
    lateinit var auth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference

    lateinit var myPostAdapter: ActiveAdapter
    var statusList = mutableListOf<StatusDataClass>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoAccountBinding.inflate(layoutInflater)
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = Firebase.auth
        setViewActivity()
        getData()
        setContentView(binding.root)
        setOnclickListener()
    }

    fun setViewActivity() {

        var current = auth.currentUser
        var id = current?.uid
        // Truy xuat thong tin trong bang User
        var userDataReference = databaseReference.child("Users")
        var query = userDataReference.child(id!!)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var name = snapshot.child("userName").value as String
                    var numberPhone = snapshot.child("numberPhone").value as String
                    var rate = snapshot.child("rate").value
                    var address = snapshot.child("userAddress").value as String
                    Log.d(javaClass.simpleName, "onDataChange: $name")
                    binding.tvAddress.text = address
                    binding.tvPhoneNumber.text = numberPhone
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

        var userIdPost = auth.currentUser?.uid
        Log.d(javaClass.simpleName, "getData: $userIdPost")
        var dataPost = databaseReference.child("Post")
        var query = dataPost.orderByChild("userPostId").equalTo(userIdPost!!)


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

    fun setOnclickListener() {
        binding.imvSetting.setOnClickListener {
            popupMenu(it)
        }
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


    fun popupMenu(view: View) {
        val popupMenu = PopupMenu(this.applicationContext, view)

        popupMenu.menuInflater.inflate(R.menu.shiow_menu_setting, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.icEdit -> {
                    editUswer()
                    true
                }

                R.id.itChangePass -> {
                    true
                } else -> false
            }
        }
        popupMenu.show()
    }

    fun editUswer() {
        Toast.makeText(this, "Chỉnh sửa thông tin", Toast.LENGTH_SHORT).show()
        val bindingEdit: UserEditBinding
        bindingEdit = UserEditBinding.inflate(LayoutInflater.from(this), null, false)
        val viewItem = bindingEdit.root
        var userName = bindingEdit.edtUserName
        var age = bindingEdit.edtAge
        var address = bindingEdit.edtAddress
        var numberPhone = bindingEdit.edtPhone

        val editUser = AlertDialog.Builder(this)
            .setView(viewItem)
        editUser.setPositiveButton("Đồng ý") {
                dialog,_->
            var name = binding.tvUserName.text.toString()
            var age = bindingEdit.edtAge.text.toString()
            var adress = bindingEdit.edtAge.text.toString()
            var numberPhone = bindingEdit.edtPhone.text.toString()

            updateAccount()
        }
        editUser.setNegativeButton("Hủy") {
                dialog,_->
            dialog.dismiss()
            Toast.makeText(this, "Hủy", Toast.LENGTH_SHORT).show()

        }
        editUser.create()
        editUser.show()
    }

    fun updateAccount() {

    }
}