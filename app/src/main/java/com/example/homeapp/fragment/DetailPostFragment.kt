package com.example.homeapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.account.UserAccounActivity
import com.example.homeapp.R
import com.example.homeapp.databinding.FragmentDetailPostragmentBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlin.math.log


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

        clickListener()

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
                binding.tvPrice.text = "Giá: $price"
                binding.tvTimeWork.setText("Thời gian: $timeString giờ")
                binding.tvAddress.text = address
                binding.tvCate.text = cate
                if (describe.isEmpty()) {
                    binding.tvDescribe.text = "Không có mô tả nào"
                } else {
                    binding.tvDescribe.text = describe
                }

                setHandlerEvent(postId, userPostId)


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun setHandlerEvent(id: String, userPostId: String) {

        /**
         * Lấy thông tin người đăng
         */
        var userData: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
        var queryUser = userData.child(userPostId)
        queryUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userName = snapshot.child("userName").value as String
                var numberPhone = snapshot.child("numberPhone").value as String
                binding.tvUserPost.text = userName
                binding.tvNumUser.text = numberPhone
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        var auth = Firebase.auth
        var uId = auth.currentUser?.uid
        if (uId == userPostId) {
            binding.btnWork.visibility = View.GONE
        } else {
            binding.btnWork.setOnClickListener {

                    val update = detailPost.child("Post").child(id)
                    update.child("state").setValue("Đã được nhận")
                        .addOnSuccessListener {

                        }
                    update.child("userWorkId").setValue(uId)
                        .addOnCompleteListener {
                            Toast.makeText(context, "Công việc đã được bạn nhận", Toast.LENGTH_SHORT).show()
                            binding.tvNumUser.visibility = View.VISIBLE
                            binding.btnWork.visibility = View.GONE
                        }

            }
        }

        binding.tvUserPost.setOnClickListener {
            var intent: Intent = Intent(context, UserAccounActivity::class.java)
            intent.putExtra("userPostId", userPostId)
            startActivity(intent)
        }

        binding.imvIconBack.setOnClickListener {
            // Trở lại Fragment trước đó khi biểu tượng được nhấp
            // Lấy ra FragmentManager của hoạt động chứa Fragment
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
//            requireContext().onBackPressed()
        }


//        binding.btnWork.setOnClickListener {
//            var auth = Firebase.auth
//            var uId = auth.currentUser?.uid
//            if (uId == userPostId) {
//                binding.btnWork.visibility = View.GONE
//            } else {
//
//                val update = detailPost.child("Post").child(id)
//                update.child("state").setValue("Đã được nhận")
//                    .addOnSuccessListener {
//
//                    }
//                update.child("userWorkId").setValue(uId)
//                    .addOnCompleteListener {
//                        Toast.makeText(context, "Công việc đã được bạn nhận", Toast.LENGTH_SHORT).show()
//                        binding.tvNumUser.visibility = View.VISIBLE
//                    }
//
//            }
//
//
//        }
    }

    fun clickListener() {
        binding.tvUserPost.setOnClickListener {
            Log.d(javaClass.simpleName, "clickListener: dđ")
        }
    }

    fun sendDataToFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransacion = fragmentManager?.beginTransaction()

        fragmentTransacion?.replace(R.id.layoutFragment, fragment)
        fragmentTransacion?.commit()
    }

}