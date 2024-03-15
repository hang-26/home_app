package com.example.homeapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.homeapp.R
import com.example.homeapp.databinding.FragmentDetailPostragmentBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class DetailPostFragment : Fragment() {
     lateinit var binding: FragmentDetailPostragmentBinding
     lateinit var detailPost: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailPostragmentBinding.inflate(layoutInflater, container, false)

        var bundle = arguments
        if (binding != null) {
            val idPost = bundle?.getString("id")
           getDataPost(idPost!!)
        }

        return binding.root
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
                binding.tvPrice.text = price
                binding.tvTimeWork.setText("$timeString giờ")
                binding.tvDescribe.text = describe
                binding.tvAddress.text = address
                binding.tvCate.text = cate

                setHandlerEvent(postId, userPostId)


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun setHandlerEvent(id: String, userPostId: String) {
        binding.btnWork.setOnClickListener {
            var auth = Firebase.auth
            var uId = auth.currentUser?.uid
            if (uId == userPostId) {
                Toast.makeText(context,
                    "Bạn không thể nhận công việc của mình",
                    Toast.LENGTH_SHORT)
                    .show()
            } else {
                val update = detailPost.child("Post").child(id)
                update.child("state").setValue("Đã được nhận")
                    .addOnSuccessListener {

                    }
                update.child("userWorkId").setValue(uId)
                    .addOnCompleteListener {
                        Toast.makeText(context, "Công việc đã được bạn nhận", Toast.LENGTH_SHORT).show()

                    }

            }


        }
    }


}