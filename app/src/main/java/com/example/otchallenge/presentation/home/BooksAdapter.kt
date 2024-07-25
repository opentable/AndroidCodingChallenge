package com.example.otchallenge.presentation.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.otchallenge.databinding.ItemBookVerticalBinding
import com.example.otchallenge.domain.entity.ItemBook

class BooksAdapter : Adapter<ItemBookVerticalHolder>() {
    private var dataSource: List<ItemBook> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBookVerticalHolder {
        val itemVertical =
            ItemBookVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemBookVerticalHolder(itemVertical)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: ItemBookVerticalHolder, position: Int) {
        holder.bind(dataSource[position])
    }

    // this is just for the sample, we should use a diff callback
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(bookList: List<ItemBook>) {
        if (dataSource.containsAll(bookList).not()) {
            dataSource = bookList
            notifyDataSetChanged()
        }
    }
}

class ItemBookVerticalHolder(private var binding: ItemBookVerticalBinding) :
    ViewHolder(binding.root) {
    fun bind(itemBook: ItemBook) {
        binding.txtTitle.text = itemBook.title
        binding.txtAuthor.text = itemBook.author
        binding.txtDescription.text = itemBook.description
        binding.txtISBN.text = itemBook.isbn
        Glide.with(binding.root).load(itemBook.bookImageUrl).into(binding.imgBook)
    }
}