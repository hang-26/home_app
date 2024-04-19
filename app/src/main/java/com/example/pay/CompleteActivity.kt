package com.example.pay

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.homeapp.R
import com.example.homeapp.databinding.ActivityCompleteBinding
import com.example.homeapp.databinding.ActivityPaymentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CompleteActivity : AppCompatActivity() {
    lateinit var binding: ActivityCompleteBinding
    lateinit var detailPost: DatabaseReference
    var rateNum: Int = 0
    var rateUser: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteBinding.inflate(layoutInflater)
        var intent = intent
        var id = intent.getStringExtra("id")
        Log.d(javaClass.simpleName, "onCreate: $id")
        getDataPost(id!!)
        setRate()
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
//                var userPostId = snapshot.child("userPostId").value as String
                var userWorkId = snapshot.child("userWorkId").value
                var userWorkIdValue = userWorkId.toString()
                binding.tvContent.text = postContent
                binding.tvPrice.text = "$price VNĐ"
                binding.tvTimeWork.setText("$timeString giờ")
                binding.tvDescribe.text = describe
                binding.tvAddress.text = address
                binding.tvCate.text = cate
                binding.tvState.text = state



                if (userWorkId == null) {
                    binding.tvWorkerName.text = "Chưa có người nhận"
                }
                else {

                    var userWokData = FirebaseDatabase.getInstance().reference.child("Users")
                    var queryUser = userWokData.child(userWorkIdValue )

                    queryUser.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            if (snapshot.exists()) {
                                var userNameWorker = snapshot.child("userName").value as String
                                Log.d(javaClass.simpleName, "onDataChange: $userNameWorker")
                                binding.tvWorkerName.text = "Người nhận việc: $userNameWorker"
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                    if (state == "Đã được nhận") {
                        binding.btnConfirm.setText("Xác nhận hoàn thành công việc")
                        binding.btnConfirm.setOnClickListener {

                            setEventPay(postId)
                            upDateUserRate(userWorkIdValue)
                        }
                    } else {
                        binding.btnConfirm.visibility = View.GONE
                    }

                }
//                upDateUserRate(userWorkIdValue)


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun setEventPay(postId: String) {

        var update = detailPost.child("Post").child(postId)
        update.child("state").setValue("Đã hoàn thành")
            .addOnSuccessListener {

            }
        Log.d(javaClass.simpleName, " $rateNum")
//        var num = rateNum.toFloat()
        update.child("rateUWork").setValue(rateNum)
            .addOnSuccessListener {
                Toast.makeText(this, "Đã đánh giá $rateNum", Toast.LENGTH_SHORT).show()

            }
        setResult(Activity.RESULT_OK, intent)
        finish()

    }

    fun setRate() {
        binding.radioGroupRate.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rbtRateOne -> {
                    binding.rbtRateOne.setBackgroundResource(R.drawable.ic_rate_star)
                    binding.rbtRateTwo.setButtonDrawable(R.drawable.ic_star)
                    binding.rbtRateThree.setButtonDrawable(R.drawable.ic_star)
                    binding.rbtRateFour.setButtonDrawable(R.drawable.ic_star)
                    binding.rbtRateFive.setButtonDrawable(R.drawable.ic_star)
                    rateNum = 1


                }
               R.id.rbtRateTwo -> {
                   binding.rbtRateOne.setButtonDrawable(R.drawable.ic_rate_star)
                   binding.rbtRateTwo.setButtonDrawable(R.drawable.ic_rate_star)
                   binding.rbtRateThree.setButtonDrawable(R.drawable.ic_star)
                   binding.rbtRateFour.setButtonDrawable(R.drawable.ic_star)
                   binding.rbtRateFive.setButtonDrawable(R.drawable.ic_star)
                   rateNum = 2
               }
                R.id.rbtRateThree -> {
                    binding.rbtRateOne.setButtonDrawable(R.drawable.ic_rate_star)
                    binding.rbtRateTwo.setButtonDrawable(R.drawable.ic_rate_star)
                    binding.rbtRateThree.setButtonDrawable(R.drawable.ic_rate_star)
                    binding.rbtRateFour.setButtonDrawable(R.drawable.ic_star)
                    binding.rbtRateFive.setButtonDrawable(R.drawable.ic_star)
                    rateNum = 3
                }
                R.id.rbtRateFour -> {
                    binding.rbtRateOne.setButtonDrawable(R.drawable.ic_rate_star)
                    binding.rbtRateTwo.setButtonDrawable(R.drawable.ic_rate_star)
                    binding.rbtRateThree.setButtonDrawable(R.drawable.ic_rate_star)
                    binding.rbtRateFour.setButtonDrawable(R.drawable.ic_rate_star)
                    binding.rbtRateFive.setButtonDrawable(R.drawable.ic_star)

                    rateNum = 4
                }
                R.id.rbtRateFive ->{
                    binding.rbtRateOne.setButtonDrawable(R.drawable.ic_rate_star)
                    binding.rbtRateTwo.setButtonDrawable(R.drawable.ic_rate_star)
                    binding.rbtRateThree.setButtonDrawable(R.drawable.ic_rate_star)
                    binding.rbtRateFour.setButtonDrawable(R.drawable.ic_rate_star)
                    binding.rbtRateFive.setButtonDrawable(R.drawable.ic_rate_star)
                    rateNum = 5
                }

            }
            Log.d(javaClass.simpleName, "setRate: $rateNum")

        }
    }

    fun upDateUserRate(idUser: String) {
        var count = 0
        var total = 0
        var query = detailPost.child("Post").orderByChild("userWorkId").equalTo(idUser)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (posSnap in snapshot.children) {
                        var queryRateString = posSnap.child("rateUWork").value
                        var queryRate = queryRateString.toString().toInt()
                        total += queryRate
                        if (queryRate > 0) {
                            count ++
                        }
                    }
                    Log.d(javaClass.simpleName, "onDataChange: $count")
                    if (count != 0)  {
                        rateUser = total.toFloat() / count
                    }

                }
                var dataRateUser = FirebaseDatabase.getInstance().reference
                var userData = dataRateUser.child("Users").child(idUser)
                var updateDataRate = userData.child("rate").setValue(rateUser)
                updateDataRate.addOnSuccessListener {
                    Toast.makeText(this@CompleteActivity, "đã thay đổi đánh giá tới người dùng", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}