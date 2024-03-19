package com.example.homeapp.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

class UserData(
    var userId: String? = null,
    var userName: String? = null,
    var userDate: String? = null,
    var userAddress: String? = null,
    var numberPhone: String? = null,
    var rate: Float? = null
) {



}