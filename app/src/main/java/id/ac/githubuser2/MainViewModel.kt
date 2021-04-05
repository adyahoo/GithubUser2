package id.ac.githubuser2

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.ac.githubuser2.databinding.ActivityMainBinding
import id.ac.githubuser2.model.User
import org.json.JSONObject
import java.lang.Exception

class MainViewModel: ViewModel() {
    val listUser = MutableLiveData<ArrayList<User>>()

    fun setUser(username: String, binding: ActivityMainBinding) {
        val listItem = ArrayList<User>()
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 09376b629bf808cf96f3412c33666345c243a798")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    binding.tvStatus.visibility = View.INVISIBLE
                    val result = String(responseBody)
                    val respObj = JSONObject(result)
                    val list = respObj.getJSONArray("items")

                    if (respObj.getInt("total_count") != 0) {
                        for (i in 0 until list.length()) {
                            val user = User()
                            user.username = list.getJSONObject(i).getString("login")
                            user.avatar = list.getJSONObject(i).getString("avatar_url")
                            listItem.add(user)
                        }
                    } else {
                        binding.tvStatus.setText(R.string.no_user_found)
                        binding.tvStatus.visibility = View.VISIBLE
                    }

                    listUser.postValue(listItem)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                error.printStackTrace()
            }
        })
    }

    fun getUser(): MutableLiveData<ArrayList<User>> {
        return listUser
    }
}