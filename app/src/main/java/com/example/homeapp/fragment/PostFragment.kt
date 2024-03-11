package com.example.homeapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.homeapp.R
import com.example.homeapp.data.PostData
import com.example.homeapp.databinding.FragmentPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class PostFragment : Fragment() {
    lateinit var binding: FragmentPostBinding
    lateinit var daRef: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var cate: String
    lateinit var job: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(layoutInflater, container, false)



        binding.btPost. setOnClickListener {
//            post()
            setPost()
        }

        setupSpinner()

        return binding.root
    }

    fun setupSpinner() {
        val list = resources.getStringArray(R.array.spinner_list)
        val cateListAdapter =
           context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, list) }

        binding.spinnerList.adapter = cateListAdapter
        binding.spinnerList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cate = list[position]

                if (cate == "Dọn dẹp") {
                    cleanSpinner()
                }

                else if (cate == "Sửa chữa") {
                    fixingSpinner()
                }
                else if (cate == "Nấu ăn") {
                    cookingSpinner()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


    }

    fun cookingSpinner() {
        val list = resources.getStringArray(R.array.spinner_cooking_job)

        val cookingAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, list) }

        binding.spinnerJob.adapter = cookingAdapter
        binding.spinnerJob.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                job = list[position]
                Toast.makeText(context, "Lựa chọn $job", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

    }

    fun fixingSpinner() {
        val list = resources.getStringArray(R.array.spinner_fixing_job)

        val fixingAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, list) }

        binding.spinnerJob.adapter = fixingAdapter
        binding.spinnerJob.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                job = list[position]
                Toast.makeText(context, "Lựa chọn $job", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

    }

    fun cleanSpinner() {
        val list = resources.getStringArray(R.array.spinner_cleaning_job)

        val jobAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, list) }

        binding.spinnerJob.adapter = jobAdapter
        binding.spinnerJob.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                job = list[position]
                Toast.makeText(context, "Lựa chọn $job", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

    }


    fun setPost() {

        auth = Firebase.auth
        daRef = FirebaseDatabase.getInstance().getReference("Post")

        var current = auth.currentUser
        Log.d(javaClass.simpleName, "onCreateView: $current")
        if (current != null) {
            val userId = current.uid
            Log.d(javaClass.simpleName, "setPost: userId = $userId")
            val postId = daRef.push().key
            Log.d(javaClass.simpleName, "setPost: $postId")

            var time = binding.edtTime.text.toString()
            var priceString = binding.tvMoney.text.toString()
            var describe = binding.tvDescribe.text.toString()
            var address = binding.tvAddress.text.toString()

            if (time.isNullOrEmpty() || priceString.isNullOrEmpty() || address.isNullOrEmpty()) {
                Toast.makeText(context, " Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                var price = priceString.toDouble()
                Log.d(javaClass.simpleName, "setPost: $price")

                val post = PostData( postId, job, cate, userId, address, null, time, price, "Đang chờ", 0f, 0f, describe)
                Log.d(javaClass.simpleName, "setPost: $post")
                daRef.child(postId!!).setValue(post)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(context, " Đăng thông tin thành công", Toast.LENGTH_SHORT).show()
                            Log.d(javaClass.simpleName, "setPost: Thành công")
                            sendDataToFragment(HomeFragment())
                        }
                    }
            }

        }

    }

    private  fun sendDataToFragment(fragment: Fragment){
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransacion = fragmentManager.beginTransaction()

        fragmentTransacion.replace(R.id.layoutFragment, fragment)
        fragmentTransacion.commit()
    }


//
//    fun post() {
//
//        auth = Firebase.auth
//        daRef = FirebaseDatabase.getInstance().getReference("Post")
//
//        var current = auth.currentUser
//        Log.d(javaClass.simpleName, "onCreateView: $current")
//        if (current != null) {
//            val userId = current.uid
//            Log.d(javaClass.simpleName, "setPost: userId = $userId")
//            val postId = daRef.push().key
//            Log.d(javaClass.simpleName, "setPost: $postId")
//
//            var cateWork = binding.tvList.text.toString()
//            var jobName = binding.tvJob.text.toString()
//            var time = binding.tvTime.text.toString()
//            var priceString = binding.tvMoney.text.toString()
//            var describe = binding.tvDescribe.text.toString()
//            var address = binding.tvAddress.text.toString()
//
//            if (cateWork.isEmpty() || jobName.isEmpty() || time.isEmpty() || priceString.isEmpty() || address.isEmpty()) {
//                Toast.makeText(context, " Không được để trống thông tin", Toast.LENGTH_SHORT).show()
//            } else {
//                var price = priceString.toDouble()
//                Log.d(javaClass.simpleName, "setPost: $price")
//
//
//                val post = PostData( postId, jobName, cateWork, userId, address, null, time, price, "Đang chờ", 0f, 0f, describe)
//                Log.d(javaClass.simpleName, "setPost: $post")
//                daRef.child(postId!!).setValue(post)
//                    .addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            Log.d(javaClass.simpleName, "setPost: Thành công")
//                        }
//                    }
//            }
//
//
//
//        }
//
//
//    }

}