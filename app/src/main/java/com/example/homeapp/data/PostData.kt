package com.example.homeapp.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


class PostData(
    var postId: String? = null,
    var postName: String? = null,
    var cateId: String? = null,
    var userPostId: String? = null,
//    var userPostName: String? = null,
    var address: String? = null,
    var userWorkId: String? = null,
    var timeWork: String? = null,
    var price: Double? = null,
    var state: String? = null,
    var rateUPost: Float? = null,
    var rateUWork: Int? = null,
    var describe: String? = null
) {


}