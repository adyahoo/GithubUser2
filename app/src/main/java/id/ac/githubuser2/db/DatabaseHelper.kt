package id.ac.githubuser2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.AVATAR
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.COMPANY
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.FOLLOWERS
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.FOLLOWINGS
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.IS_FAV
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.NAME
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.REPO
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.USERNAME
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion._ID

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "db_user"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME" +
                " ($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $NAME TEXT NOT NULL," +
                " $USERNAME TEXT NOT NULL," +
                " $COMPANY TEXT NOT NULL," +
                " $AVATAR TEXT NOT NULL," +
                " $FOLLOWERS INTEGER NOT NULL," +
                " $FOLLOWINGS INTEGER NOT NULL," +
                " $REPO INTEGER NOT NULL," +
                " $IS_FAV BOOLEAN NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}