package com.example.otchallenge.presentation.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.otchallenge.R
import com.example.otchallenge.presentation.model.BookPresentation
import com.example.otchallenge.utils.GlideApp

class BookAdapter(private val onClick: (Int) -> Unit) :
    ListAdapter<BookPresentation, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book_gallery, parent, false)
        return BookViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BookViewHolder(itemView: View, val onClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.item_book_title)
        private val descriptionTextView: TextView =
            itemView.findViewById(R.id.item_book_description)
        private val bookImageView: ImageView = itemView.findViewById(R.id.item_book_cover)

        private lateinit var currentBook: BookPresentation

        init {
            itemView.setOnClickListener {
                onClick(currentBook.id)
            }
        }

        fun bind(book: BookPresentation) {
            currentBook = book
            titleTextView.text = book.title
            descriptionTextView.text = book.description
            GlideApp.with(itemView.context)
                .load(book.bookImage)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(bookImageView)
        }
    }
}

class BookDiffCallback : DiffUtil.ItemCallback<BookPresentation>() {
    override fun areItemsTheSame(oldItem: BookPresentation, newItem: BookPresentation): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: BookPresentation, newItem: BookPresentation): Boolean {
        return oldItem == newItem
    }
}