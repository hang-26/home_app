package com.example.homeapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.account.InfoAccountActivity
import com.example.homeapp.R
import com.example.homeapp.databinding.FragmentAccountBinding
import com.example.homeapp.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class AccountFragment : Fragment() {
    lateinit var binding: FragmentAccountBinding
    lateinit var auth:FirebaseAuth
    lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)

        auth = Firebase.auth
        databaseReference = FirebaseDatabase.getInstance().reference
        // Inflate the layout for this fragment
        setInterface()

        binding.btSignOut.setOnClickListener {
            signOut()

        }

        binding.tvPosted.setOnClickListener {
            sendDataToFragment(ActiveFragment())
        }

        binding.ivAvatar.setOnClickListener {
            inForAccount()
        }

        binding.tvName.setOnClickListener {
            inForAccount()
        }

        return binding.root

    }

    fun setInterface() {
        var current = auth.currentUser
        var uId = current?.uid
        var userDatabase = databaseReference.child("Users")
        var query = userDatabase.child(uId!!)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    var userName = snapshot.child("userName").value as String
                    var rateString = snapshot.child("rate"). value

                    binding.tvName.text = userName
                    binding.tvRating.text = "Đánh giá $rateString"
                    if (rateString != null) {
                        var rate = rateString.toString().toFloat()
                        setRate(rate)
                    }
//                    else {
//                        binding.tvRating.text = "Chưa có đánh giá"
////                        binding.imvRate1.visibility = View.GONE
////                        binding.imvRate2.visibility = View.GONE
////                        binding.imvRate3.visibility = View.GONE
////                        binding.imvRate4.visibility = View.GONE
////                        binding.imvRate5.visibility = View.GONE
//                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    fun sendDataToFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransacion = fragmentManager?.beginTransaction()

        fragmentTransacion?.replace(R.id.layoutFragment, fragment)
        fragmentTransacion?.commit()
    }


    fun inForAccount() {
        var intent = Intent(context, InfoAccountActivity::class.java)
        startActivity(intent)
    }

    private fun signOut() {

        auth.signOut()

        // Quay trở lại màn hình đăng nhập (Login)
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    fun setRate(rate: Float) {
       if (rate == 1f) {
           binding.imvRate1.setImageResource(R.drawable.rating_full)
           binding.imvRate2.setImageResource(R.drawable.rating_no)
           binding.imvRate3.setImageResource(R.drawable.rating_no)
           binding.imvRate4.setImageResource(R.drawable.rating_no)
           binding.imvRate5.setImageResource(R.drawable.rating_no)
       } else if(1f< rate && rate < 2f) {
           binding.imvRate1.setImageResource(R.drawable.rating_full)
           binding.imvRate2.setImageResource(R.drawable.rating_not_full)
           binding.imvRate3.setImageResource(R.drawable.rating_no)
           binding.imvRate4.setImageResource(R.drawable.rating_no)
           binding.imvRate5.setImageResource(R.drawable.rating_no)
       } else if( rate == 2f) {
           binding.imvRate1.setImageResource(R.drawable.rating_full)
           binding.imvRate2.setImageResource(R.drawable.rating_full)
           binding.imvRate3.setImageResource(R.drawable.rating_no)
           binding.imvRate4.setImageResource(R.drawable.rating_no)
           binding.imvRate5.setImageResource(R.drawable.rating_no)
       } else if(2f< rate && rate < 3f) {
           binding.imvRate1.setImageResource(R.drawable.rating_full)
           binding.imvRate2.setImageResource(R.drawable.rating_full)
           binding.imvRate3.setImageResource(R.drawable.rating_not_full)
           binding.imvRate4.setImageResource(R.drawable.rating_no)
           binding.imvRate5.setImageResource(R.drawable.rating_no)
       } else if(rate == 3f) {
           binding.imvRate1.setImageResource(R.drawable.rating_full)
           binding.imvRate2.setImageResource(R.drawable.rating_full)
           binding.imvRate3.setImageResource(R.drawable.rating_full)
           binding.imvRate4.setImageResource(R.drawable.rating_no)
           binding.imvRate5.setImageResource(R.drawable.rating_no)
       } else if(3f< rate && rate < 4f) {
           binding.imvRate1.setImageResource(R.drawable.rating_full)
           binding.imvRate2.setImageResource(R.drawable.rating_full)
           binding.imvRate3.setImageResource(R.drawable.rating_full)
           binding.imvRate4.setImageResource(R.drawable.rating_not_full)
           binding.imvRate5.setImageResource(R.drawable.rating_no)
       } else if( rate == 4f) {
           binding.imvRate1.setImageResource(R.drawable.rating_full)
           binding.imvRate2.setImageResource(R.drawable.rating_full)
           binding.imvRate3.setImageResource(R.drawable.rating_full)
           binding.imvRate4.setImageResource(R.drawable.rating_full)
           binding.imvRate5.setImageResource(R.drawable.rating_no)
       }else if(4f< rate && rate < 5f) {
           binding.imvRate1.setImageResource(R.drawable.rating_full)
           binding.imvRate2.setImageResource(R.drawable.rating_full)
           binding.imvRate3.setImageResource(R.drawable.rating_full)
           binding.imvRate4.setImageResource(R.drawable.rating_not_full)
           binding.imvRate5.setImageResource(R.drawable.rating_no)
       }else if( rate == 5f) {
           binding.imvRate1.setImageResource(R.drawable.rating_full)
           binding.imvRate2.setImageResource(R.drawable.rating_full)
           binding.imvRate3.setImageResource(R.drawable.rating_full)
           binding.imvRate4.setImageResource(R.drawable.rating_full)
           binding.imvRate5.setImageResource(R.drawable.rating_full)
       }
    }

}