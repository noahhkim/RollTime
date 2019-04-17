package com.noahkim.rolltime.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Noah on 10/5/2017.
 */

class Video protected constructor(`in`: Parcel) : Parcelable {

    var title: String? = null
    var thumbnail: String? = null
    var videoId: String? = null

    init {
        title = `in`.readString()
        thumbnail = `in`.readString()
        videoId = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(title)
        parcel.writeString(thumbnail)
        parcel.writeString(videoId)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Video> = object : Parcelable.Creator<Video> {
            override fun createFromParcel(`in`: Parcel): Video {
                return Video(`in`)
            }

            override fun newArray(size: Int): Array<Video?> {
                return arrayOfNulls(size)
            }
        }
    }
}
