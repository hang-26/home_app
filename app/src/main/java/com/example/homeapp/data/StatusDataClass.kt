package com.example.homeapp.data

import java.net.Inet4Address

class StatusDataClass {
    var postId: String? = null;
    var postName: String? = null;
    var cateId: String? = null;
    var userPostId: String? = null;
    var address: String? = null;
    var userWorkId: String? = null;
    var timeWork: String? = null;
    var price: Double? = null;
    var state: String? = null;
    var rateUPost: Float? = null;
    var rateUWork: Float? = null;
    var describe: String? = null;

    var namePost: String? = null;
    var nameWork: String? = null

    constructor(postId: String?,
                postName: String?,
                cate: String?,
                userPostId: String?,
                address: String?,
                userWorkId:String?,
                timeWord: String?,
                price: Double?,
                state: String?,
                rateUPost: Float?,
                rateUWork: Float?,
                describe: String?
    ) {
        this.postId = postId
        this.postName = postName
        this.cateId = cate
        this.userPostId = userPostId
        this.address = address
        this.userWorkId = userWorkId
        this.timeWork = timeWord
        this.price = price
        this.state = state
        this.rateUPost = rateUPost
        this.rateUWork = rateUWork
        this.describe = describe
    }

    constructor(

                postName: String?,
                cate: String?,
                userPostId: String?,
                address: String?,
                timeWork: String?,
                price: Double?,
                state: String?,
                describe: String?) {
        this.postName = postName
        this.cateId = cate
        this.userPostId = userPostId
        this.address = address
        this.timeWork = timeWork
        this.price = price
        this.state = state
        this.describe = describe
    }

    constructor( postId: String?,
                postName: String?,
                state: String,
                namePost: String?,
                price: Double?) {
        this.postId = postId
        this.postName = postName
        this.state = state
        this.namePost = namePost
        this.price = price
    }

    constructor(
        postName: String?,
        cate: String?,
        address: String?,
        timeWork: String?,
        price: Double?,
        state: String?,
        describe: String?) {
        this.postName = postName
        this.cateId = cate
        this.address = address
        this.timeWork = timeWork
        this.price = price
        this.state = state
        this.describe = describe
    }
}