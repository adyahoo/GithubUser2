package id.ac.githubuser2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.githubuser2.adapter.OnItemClickCallback
import id.ac.githubuser2.adapter.UserAdapter
import id.ac.githubuser2.databinding.ActivityFavoriteUserBinding
import id.ac.githubuser2.db.UserHelper
import id.ac.githubuser2.helper.MappingHelper
import id.ac.githubuser2.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorite Users"

        userAdapter = UserAdapter()
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.adapter = userAdapter

        getUserFav()

        userAdapter.setOnItemClickCallback(object : OnItemClickCallback{
            override fun onItemClicked(data: User) {
                moveToDetail(data)
            }
        })
    }

    private fun moveToDetail(data: User) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_OBJECT, data)
        startActivity(intent)
    }

    private fun getUserFav() {
        userHelper = UserHelper(this)
        userHelper.open()
        GlobalScope.launch(Dispatchers.Main) {
            val defferedUser = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val userList = defferedUser.await()
            setProgBar(false)

            if (userList.size > 0) {
                userAdapter.setData(userList)
            } else {
                userList.clear()
                userAdapter.setData(userList)
                binding.tvStatusFav.visibility = View.VISIBLE
            }
        }
    }

    private fun setProgBar(state: Boolean) {
        if (state) {
            binding.favoriteProgbar.visibility = View.VISIBLE
        } else {
            binding.favoriteProgbar.visibility = View.INVISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        getUserFav()
    }
}