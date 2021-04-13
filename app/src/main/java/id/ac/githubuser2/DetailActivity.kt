package id.ac.githubuser2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.contentValuesOf
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import id.ac.githubuser2.adapter.DetailSectionPagerAdapter
import id.ac.githubuser2.databinding.ActivityDetailBinding
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.AVATAR
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.COMPANY
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.FOLLOWERS
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.FOLLOWINGS
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.IS_FAV
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.NAME
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.REPO
import id.ac.githubuser2.db.DatabaseContract.UserColumns.Companion.USERNAME
import id.ac.githubuser2.db.UserHelper
import id.ac.githubuser2.model.User
import java.io.IOException

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var userHelper: UserHelper
    private lateinit var user: User
    companion object{
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
        const val EXTRA_OBJECT = "object"
    }
    private var isFav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra<User>(EXTRA_OBJECT)
        val title = supportActionBar
        title?.title = user.username
        title?.setDisplayHomeAsUpEnabled(true)

        val sectionPagerAdapter = DetailSectionPagerAdapter(this, user.username.toString())
        val viewPager = binding.detailViewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs = binding.detailTabs

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        getFavUser(user.username.toString())
        setFavButton(isFav)

        binding.fabFav.setOnClickListener {
            if (isFav) {
                deleteUser(user.username.toString())
                setFavButton(false)
                isFav = false
            } else {
                insertUser()
                setFavButton(true)
                isFav = true
            }
        }

        TabLayoutMediator(tabs, viewPager){
            tab, position -> tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        setLayout(user)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertUser() {
        val values = contentValuesOf(
                USERNAME to user.username,
                NAME to user.name,
                COMPANY to user.company,
                AVATAR to user.avatar,
                FOLLOWERS to user.follower,
                FOLLOWINGS to user.following,
                REPO to user.repo,
                IS_FAV to true
        )
        userHelper.insert(values)
        Toast.makeText(this, "Add to Favorite Success", Toast.LENGTH_SHORT).show()
    }

    private fun deleteUser(username: String) {
        userHelper.deleteByUsername(username)
        Toast.makeText(this, "Remove from Favorite Success", Toast.LENGTH_SHORT).show()
    }

    private fun getFavUser(username: String) {
        try {
            val cursor = userHelper.queryByName(username)
            isFav = cursor.count > 0
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setFavButton(isFav: Boolean) {
        if (isFav) {
            binding.fabFav.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.fabFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun setLayout(user: User) {
        binding.tvName.text = user.name
        binding.tvCompany.text = user.company
        binding.tvRepo.text = user.repo.toString()
        binding.tvFollower.text = user.follower.toString()
        binding.tvFollowing.text = user.following.toString()
        Glide.with(this)
            .load(user.avatar)
            .into(binding.civUserDetail)
    }
}