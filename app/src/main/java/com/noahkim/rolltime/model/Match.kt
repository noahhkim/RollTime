package com.noahkim.rolltime.model

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by noahkim on 8/23/17.
 */

@Keep
@IgnoreExtraProperties
data class Match(var oppName: String? = null,
            var oppBeltLevel: Int? = null,
            var userChokeCount: Int? = null,
            var userArmlockCount: Int? = null,
            var userLeglockCount: Int? = null,
            var oppChokeCount: Int? = null,
            var oppArmlockCount: Int? = null,
            var oppLeglockCount: Int? = null) {

//    @Exclude
//    fun toMap(): Map<String, Any> {
//        val result = HashMap<String, Any>()
//        result["oppName"] = oppName
//        result["oppBeltLevel"] = oppBeltLevel
//        result["userChokeCount"] = userChokeCount
//        result["userArmlockCount"] = userArmlockCount
//        result["userLeglockCount"] = userLeglockCount
//        result["oppChokeCount"] = oppChokeCount
//        result["oppArmlockCount"] = oppArmlockCount
//        result["oppLeglockCount"] = oppLeglockCount
//
//        return result
//    }

    companion object {
        var WHITE_BELT = 0
        var BLUE_BELT = 1
        var PURPLE_BELT = 2
        var BROWN_BELT = 3
        var BLACK_BELT = 4
    }

}
