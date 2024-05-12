package com.example.pay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.homeapp.databinding.ActivityWorkedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WorkedActivity : AppCompatActivity() {
    lateinit var binding: ActivityWorkedBinding
    lateinit var detailPost: DatabaseReference
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkedBinding.inflate(layoutInflater)
        var intent = intent
        var id  = intent.getStringExtra("id")
        getDataPost(id!!)
        setContentView(binding.root)
    }

    fun getDataPost(id: String) {
        detailPost = FirebaseDatabase.getInstance().reference
        val query = detailPost.child("Post").child(id)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postContent = snapshot.child("postName").value as String
                val address = snapshot.child("address").value as String
                val describe = snapshot.child("describe").value as String
                var timeString = snapshot.child("timeWork").value as String
                var priceString = snapshot.child("price").value
                var price = priceString.toString()
                var state = snapshot.child("state").value as String
                var cate = snapshot.child("cateId").value as String
                var postId = snapshot.child("postId").value as String

                var userPostId = snapshot.child("userPostId").value as String

                binding.tvContent.text = postContent
                binding.tvPrice.text = "$price VNĐ"
                binding.tvTimeWork.setText("$timeString giờ")
                binding.tvDescribe.text = describe
                binding.tvAddress.text = address
                binding.tvCate.text = cate
                binding.tvState.text = state

                var userWokData = FirebaseDatabase.getInstance().reference.child("Users")
                var queryUser = userWokData.child(userPostId )

                queryUser.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()) {
                            var userNamePost = snapshot.child("userName").value as String
                            Log.d(javaClass.simpleName, "onDataChange: $userNamePost")
                            binding.tvWorkerName.text = "Người đăng việc: $userNamePost"
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

//                if (state == "Đã được nhận") {
//                    binding.btnConfirm.setText("Xác nhận hoàn thành công việc")
//                    binding.btnConfirm.setOnClickListener {
//                        setEventPay(postId)
//                    }
//                } else {
//                    binding.btnConfirm.visibility = View.GONE
//                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

//    fun setEventPay(id: String) {
//        var update = detailPost.child("Post").child(id).child("state").setValue("Đã hoàn thành")
//        update.addOnSuccessListener {
//            Toast.makeText(this, "Cảm ơn bạn đã xác nhận thanh toán", Toast.LENGTH_SHORT).show()
//            finish()
//        }
//    }
}