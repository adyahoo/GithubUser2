package id.ac.githubuser2.db

import android.provider.BaseColumns

class DatabaseContract {
    class UserColumns : BaseColumns {
        companion object{
            const val TABLE_NAME = "tb_user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val NAME = "name"
            const val COMPANY = "company"
            const val AVATAR = "avatar"
            const val FOLLOWERS = "followers"
            const val FOLLOWINGS = "followings"
            const val REPO = "repositories"
            const val IS_FAV = "is_fav"
        }
    }
}