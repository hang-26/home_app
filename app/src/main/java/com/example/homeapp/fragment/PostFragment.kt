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
import com.google.android.play.integrity.internal.w
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class PostFragment : Fragment() {
    lateinit var binding: FragmentPostBinding
    lateinit var daRef: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var cate: String
    lateinit var job: String
    lateinit var district: String
    lateinit var ward: String


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
        setAddress()
        return binding.root
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
//            var address = binding.tvAddress.text.toString()
            var address = "$ward, $district, Hà Nội"

            if (time.isNullOrEmpty() || priceString.isNullOrEmpty()) {
                Toast.makeText(context, " Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                var price = priceString.toDouble()
                Log.d(javaClass.simpleName, "setPost: $price")

                val post = PostData( postId, job, cate, userId, address, null, time, price, "Đang chờ", 0f, 0, describe)
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

 fun setAddress() {
     val listDistrict = resources.getStringArray(R.array.spinner_district)
     val districtListAdapter =
         context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, listDistrict) }

     binding.spinnerDistrict.adapter = districtListAdapter
     binding.spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
         override fun onItemSelected(
             parent: AdapterView<*>?,
             view: View?,
             position: Int,
             id: Long
         ) {
             district = listDistrict[position]

             if (district == "quận Hoàng Mai") {
                 Log.d(javaClass.simpleName, "onItemSelected: $district")
                 setHoangMai()
             }

             else if (district == "quận Thanh Xuân") {
                 Log.d(javaClass.simpleName, "onItemSelected: $district")

                 setThanhXuan()
             }
             else if (district == "quận Hai Bà Trưng") {
                 Log.d(javaClass.simpleName, "onItemSelected: $district")

                 setDongDa()
             }
             else if (district == "quận Đống Đa") {
                 Log.d(javaClass.simpleName, "onItemSelected: $district")

                 setHaiBaTrung()
             }

         }

         override fun onNothingSelected(parent: AdapterView<*>?) {

         }
     }
 }

    fun setHoangMai() {
        val list = resources.getStringArray(R.array.spinner_hm)

        val hmAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, list) }

        binding.spinnerWard.adapter = hmAdapter
        binding.spinnerWard.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                ward = list[position]
                Toast.makeText(context, "Lựa chọn $ward", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    fun setThanhXuan() {
        val list = resources.getStringArray(R.array.spinner_tx)

        val txAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, list) }

        binding.spinnerWard.adapter = txAdapter
        binding.spinnerWard.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                ward = list[position]
                Toast.makeText(context, "Lựa chọn $ward", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }


    fun setDongDa() {
        val list = resources.getStringArray(R.array.spinner_dd)

        val ddAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, list) }

        binding.spinnerWard.adapter = ddAdapter
        binding.spinnerWard.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                ward = list[position]
                Toast.makeText(context, "Lựa chọn $ward", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    fun setHaiBaTrung() {
        val list = resources.getStringArray(R.array.spinner_hbt)

        val hbtAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, list) }

        binding.spinnerWard.adapter = hbtAdapter
        binding.spinnerWard.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                ward = list[position]
                Toast.makeText(context, "Lựa chọn $ward", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

}