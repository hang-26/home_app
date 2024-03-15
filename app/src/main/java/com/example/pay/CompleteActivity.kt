package com.example.pay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.homeapp.databinding.ActivityPaymentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CompleteActivity : AppCompatActivity() {
    lateinit var binding: ActivityPaymentBinding
    lateinit var detailPost: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        var intent = intent
        var id = intent.getStringExtra("id")
        Log.d(javaClass.simpleName, "onCreate: $id")
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
                if (state == "Đã được nhận") {
                    binding.btnConfirm.setText("Xác nhận hoàn thành công việc")
                    binding.btnConfirm.setOnClickListener {
                        setEventPay(postId)
                    }
                } else {
                    binding.btnConfirm.visibility = View.GONE
                }

//                setHandlerEvent(postId, userPostId)


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun setEventPay(id: String) {
        var update = detailPost.child("Post").child(id).child("state").setValue("Đã hoàn thành")
        update.addOnSuccessListener {
            Toast.makeText(this, "Xác nhận thành công", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}