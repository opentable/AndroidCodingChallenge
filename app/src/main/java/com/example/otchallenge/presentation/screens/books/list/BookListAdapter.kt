package com.example.otchallenge.presentation.screens.books.list

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.otchallenge.data.model.Book
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
            binding.txtBookTitle.text = book?.title
            binding.txtBookAuthor.text = book?.author
            binding.txtBookDescription.text = book?.description

            binding.imgBookCover.isVisible = false
            binding.lytBookCoverLoading.root.isVisible = true
            binding.lytBookCoverLoadFailed.root.isVisible = false

            Glide.with(itemView.context)
                .load(book?.imageUrl)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imgBookCover.isVisible = false
                        binding.lytBookCoverLoading.root.isVisible = false
                        binding.lytBookCoverLoadFailed.root.isVisible = true
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imgBookCover.isVisible = true
                        binding.lytBookCoverLoading.root.isVisible = false
                        binding.lytBookCoverLoadFailed.root.isVisible = false
                        return false
                    }
                })
                .into(binding.imgBookCover)
        }
    }
}