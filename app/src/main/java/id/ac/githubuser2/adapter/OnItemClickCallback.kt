package id.ac.githubuser2.adapter

import id.ac.githubuser2.model.User

interface OnItemClickCallback {
    fun onItemClicked(data: User)
}