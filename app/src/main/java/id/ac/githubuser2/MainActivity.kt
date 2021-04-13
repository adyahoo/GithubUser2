package id.ac.githubuser2

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.ac.githubuser2.adapter.OnItemClickCallback
import id.ac.githubuser2.adapter.UserAdapter
import id.ac.githubuser2.databinding.ActivityMainBinding
import id.ac.githubuser2.model.User
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var userAdapter: UserAdapter
    private lateinit var binding: ActivityMainBinding
    private var listUser: ArrayList<User> = ArrayList<User>()
    private lateinit var mViewModel: MainViewModel
    companion object{
        const val TOKEN = "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"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()

        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showProgressBar(true)
                mViewModel.setUser(query, binding)
                searchView.clearFocus()
                mViewModel.getUser().observe(this@MainActivity, { userItems ->
                    if (userItems != null) {
                        showRecycler(userItems)
                        showProgressBar(false)
                    }
                })
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    clearUser()
                    when {
                        newText.isEmpty() -> {
                            binding.tvStatus.setText(R.string.search_status)
                            binding.tvStatus.visibility = View.VISIBLE
                            showProgressBar(false)
                        }
                        newText.length >= 5 -> {
                            binding.tvStatus.visibility = View.INVISIBLE
                            showProgressBar(true)
                            mViewModel.setUser(newText, binding)
                            mViewModel.getUser().observe(this@MainActivity, { userItems ->
                                if (userItems != null) {
                                    showRecycler(userItems)
                                    showProgressBar(false)
                                }
                            })
                        }
                        else -> {
                            binding.tvStatus.visibility = View.INVISIBLE
                            showProgressBar(true)
                        }
                    }
                }
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clearUser() {
        listUser.clear()
        userAdapter.setData(listUser)
    }

    private fun showRecycler(listUser: ArrayList<User>) {
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = userAdapter

        userAdapter.setData(listUser)

        userAdapter.setOnItemClickCallback(object : OnItemClickCallback {
            override fun onItemClicked(data: User) {
                getDetailUser(data.username.toString())
            }
        })
    }

    private fun getDetailUser(username: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $TOKEN")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val respObj = JSONObject(result)
                    val user = User()
                    user.username = respObj.getString("login")
                    user.repo = respObj.getInt("public_repos")
                    user.company = respObj.getString("company")
                    user.follower = respObj.getInt("followers")
                    user.following = respObj.getInt("following")
                    user.avatar = respObj.getString("avatar_url")
                    user.name = respObj.getString("name")
                    moveDetail(user)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                error?.printStackTrace()
            }
        })
    }

    private fun moveDetail(user: User) {
        val move = Intent(this, DetailActivity::class.java)
        move.putExtra(DetailActivity.EXTRA_OBJECT   , user)
        startActivity(move)
    }

    private fun showProgressBar(state: Boolean) {
        if (state) {
            binding.mainProgbar.visibility = View.VISIBLE
        } else {
            binding.mainProgbar.visibility = View.INVISIBLE
        }
    }
}