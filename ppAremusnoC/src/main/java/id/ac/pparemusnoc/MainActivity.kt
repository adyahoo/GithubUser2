package id.ac.pparemusnoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.pparemusnoc.adapter.OnItemClickCallback
import id.ac.pparemusnoc.adapter.UserAdapter
import id.ac.pparemusnoc.databinding.ActivityMainBinding
import id.ac.pparemusnoc.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import id.ac.pparemusnoc.helper.MappingHelper
import id.ac.pparemusnoc.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    companion object{
        const val TOKEN = "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"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Consumer App"

        userAdapter = UserAdapter()
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = userAdapter

        loadUserAsync()

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

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.mainProgbar.visibility = View.VISIBLE
            val defferedUser = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val userList = defferedUser.await()
            binding.mainProgbar.visibility = View.INVISIBLE

            if (userList.size > 0) {
                userAdapter.setData(userList)
            } else {
                userList.clear()
                userAdapter.setData(userList)
                Toast.makeText(applicationContext, "No Data in SQLite", Toast.LENGTH_SHORT).show()
            }
        }
    }
}