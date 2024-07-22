package com.example.otchallenge.books.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.otchallenge.R

class BooksAdapter(
    private val glide: RequestManager
): ListAdapter<BookUiModel, BooksAdapter.ViewHolder>(differCallback) {

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<BookUiModel>(){
            override fun areItemsTheSame(oldItem: BookUiModel, newItem: BookUiModel): Boolean {
                return  oldItem.author + oldItem.title == newItem.author + newItem.title
            }

            override fun areContentsTheSame(oldItem: BookUiModel, newItem: BookUiModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item,parent, false)
         return ViewHolder(view, glide)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class ViewHolder(view: View, private val glide: RequestManager): RecyclerView.ViewHolder(view) {

        private val image = view.findViewById<ImageView>(R.id.book_image_iv)
        private val title = view.findViewById<TextView>(R.id.title_tv)
        private val description = view.findViewById<TextView>(R.id.description_tv)
        private val author = view.findViewById<TextView>(R.id.author_tv)
        private val publisher = view.findViewById<TextView>(R.id.publisher_tv)
        private val isbns = view.findViewById<TextView>(R.id.isbns_tv)

        fun bind(book: BookUiModel) {
            glide.load(book.image).override(book.imageWidth, book.imageHeight).centerCrop().into(image)
            title.text = book.title
            description.text = book.description
            author.text = book.author
            publisher.text = book.publisher
            isbns.text = book.isbns
        }
    }
}