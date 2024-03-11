package com.example.homeapp.fragment

import android.app.UiAutomation
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.account.InForAccounActivity
import com.example.homeapp.R
import com.example.homeapp.databinding.FragmentAccountBinding
import com.example.homeapp.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
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
                    binding.tvRating.text = rateString.toString()

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
        var intent = Intent(context, InForAccounActivity::class.java)
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


}