package com.example.otchallenge.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.otchallenge.data.entity.Book
import com.example.otchallenge.databinding.ItemBookBinding

class BookListAdapter : PagingDataAdapter<Book, BookListAdapter.ItemViewHolder>(Comparator) {

    companion object {
        private val Comparator = object: DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        return ItemBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).let { binding ->
            ItemViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.book = getItem(position)
    }

    class ItemViewHolder(
        private val binding: ItemBookBinding
    ) : ViewHolder(binding.root) {

        var book: Book? = null
            set(value) {
                updateView(value)
                field = value
            }

        private fun updateView(book: Book?) {
            binding.bookTitle.text = book?.title
            binding.bookAuthor.text = book?.author
            binding.bookDescription.text = book?.description

            Glide.with(itemView.context)
                .load(book?.imageUrl)
                .into(binding.bookCover)
        }
    }
}