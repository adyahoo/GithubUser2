package id.ac.githubuser2.model

import android.os.Parcel
import android.os.Parcelable

data class User(
        var username: String? = null,
        var name: String? = null,
        var follower: Int = 0,
        var following: Int = 0,
        var repo: Int = 0,
        var avatar: String? = null,
        var company: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(name)
        parcel.writeInt(follower)
        parcel.writeInt(following)
        parcel.writeInt(repo)
        parcel.writeString(avatar)
        parcel.writeString(company)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}