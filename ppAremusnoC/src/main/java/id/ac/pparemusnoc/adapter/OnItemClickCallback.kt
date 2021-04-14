package id.ac.pparemusnoc.adapter

import id.ac.pparemusnoc.model.User

interface OnItemClickCallback {
    fun onItemClicked(data: User)
}