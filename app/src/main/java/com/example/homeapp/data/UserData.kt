package com.example.homeapp.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

class UserData
{
    var userId: String? = null;
    var userName: String? = null;
    var userDate: String? = null;
    var userAddress: String? = null;
    var numberPhone: String? = null;
    var rate: Float? = null

    constructor(userId: String, userName: String, userDate: String, userAddress: String, numberPhone: String, rate: Float)
    {
        this.userId = userId
        this.userName = userName
        this.userDate = userDate
        this.userAddress = userAddress
        this.numberPhone = numberPhone
        this.rate = rate
    }
    constructor(userName: String, userDate: String, userAddress: String, numberPhone: String, rate: Float)
    {

        this.userName = userName
        this.userDate = userDate
        this.userAddress = userAddress
        this.numberPhone = numberPhone
        this.rate = rate

    }
    constructor(userId: String, userName: String, userDate: String, userAddress: String, numberPhone: String)
    {
        this.userId = userId
        this.userName = userName
        this.userDate = userDate
        this.userAddress = userAddress
        this.numberPhone = numberPhone
    }

}