package id.ac.githubuser2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.githubuser2.R
import id.ac.githubuser2.databinding.ItemUserBinding
import id.ac.githubuser2.model.User
import java.lang.Exception

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var mData = ArrayList<User>()
    private var onItemClickCallback: OnItemClickCallback? = null

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)

        fun bind(user: User) {
            with(itemView) {
                binding.tvUsername.text = user.username
                Glide.with(context)
                        .load(user.avatar)
                        .into(binding.civUserAvatar)

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        try {
            return mData.size
        } catch (e: Exception) {
            return 0
        }

    }
}