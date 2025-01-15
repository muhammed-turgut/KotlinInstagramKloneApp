package com.muhammedturgut.kotlininstagramkloneapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammedturgut.kotlininstagramkloneapp.databinding.RecyclerRowBinding
import com.muhammedturgut.kotlininstagramkloneapp.model.Post
import com.squareup.picasso.Picasso

class FeedAdapter(val postList : ArrayList<Post>) :RecyclerView.Adapter<FeedAdapter.PostHolder>() {
    class PostHolder(val binding: RecyclerRowBinding) :RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.emailRecyclerRow.text=postList.get(position).email
        holder.binding.commentRecyclerRow.text=postList.get(position).comment
        Picasso.get().load(postList.get(position).dowlandUrl).into(holder.binding.imageRecyclerRow)

    }
}