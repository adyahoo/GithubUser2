package id.ac.pparemusnoc.helper

import android.database.Cursor
import id.ac.pparemusnoc.db.DatabaseContract.UserColumns.Companion.AVATAR
import id.ac.pparemusnoc.db.DatabaseContract.UserColumns.Companion.COMPANY
import id.ac.pparemusnoc.db.DatabaseContract.UserColumns.Companion.FOLLOWERS
import id.ac.pparemusnoc.db.DatabaseContract.UserColumns.Companion.FOLLOWINGS
import id.ac.pparemusnoc.db.DatabaseContract.UserColumns.Companion.NAME
import id.ac.pparemusnoc.db.DatabaseContract.UserColumns.Companion.REPO
import id.ac.pparemusnoc.db.DatabaseContract.UserColumns.Companion.USERNAME
import id.ac.pparemusnoc.db.DatabaseContract.UserColumns.Companion._ID
import id.ac.pparemusnoc.model.User

object MappingHelper {
    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val name = getString(getColumnIndexOrThrow(NAME))
                val company = getString(getColumnIndexOrThrow(COMPANY))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val followers = getInt(getColumnIndexOrThrow(FOLLOWERS))
                val followings = getInt(getColumnIndexOrThrow(FOLLOWINGS))
                val repo = getInt(getColumnIndexOrThrow(REPO))

                val user = User(id, username, name, followers, followings, repo, avatar, company)
                userList.add(user)
            }
        }
        return userList
    }
}