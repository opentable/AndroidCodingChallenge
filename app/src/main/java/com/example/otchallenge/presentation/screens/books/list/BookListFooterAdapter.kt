package com.example.otchallenge.presentation.screens.books.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.otchallenge.databinding.ItemListFooterBinding

class BookListFooterAdapter(
    private val onDetailsClick: (LoadState.Error) -> Unit,
    private val onRetryClick: () -> Unit
) : LoadStateAdapter<BookListFooterAdapter.FooterViewHolder>() {

    override fun onBindViewHolder(
        holder: FooterViewHolder,
        loadState: LoadState
    ) {
        when (loadState) {
            is LoadState.Loading -> holder.showLoading()
            is LoadState.Error -> holder.showLoadingFailed(loadState)
            is LoadState.NotLoading -> holder.hideAll()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): FooterViewHolder {
        return ItemListFooterBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            .let { binding ->
                FooterViewHolder(binding, onDetailsClick, onRetryClick)
            }
    }

    class FooterViewHolder (
        private val binding: ItemListFooterBinding,
        private val onDetailsClick: (LoadState.Error) -> Unit,
        onRetryClick: () -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.lytLoadingFailed.btnRetry.setOnClickListener { onRetryClick() }
        }

        fun showLoading() {
            binding.lytLoading.root.isVisible = true
            binding.lytLoadingFailed.root.isVisible = false
        }

        fun showLoadingFailed(loadState: LoadState.Error) {
            binding.lytLoading.root.isVisible = false
            binding.lytLoadingFailed.root.isVisible = true
            binding.lytLoadingFailed.btnDetails.setOnClickListener {
                onDetailsClick(loadState)
            }
        }

        fun hideAll() {
            binding.lytLoading.root.isVisible = false
            binding.lytLoadingFailed.root.isVisible = false
        }

    }

}