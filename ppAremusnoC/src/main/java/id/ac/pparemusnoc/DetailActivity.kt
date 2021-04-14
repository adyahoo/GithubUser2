package id.ac.pparemusnoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import id.ac.pparemusnoc.adapter.DetailSectionPagerAdapter
import id.ac.pparemusnoc.databinding.ActivityDetailBinding
import id.ac.pparemusnoc.model.User

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var user: User
    companion object{
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
        const val EXTRA_OBJECT = "object"
    }

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