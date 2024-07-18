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
            is LoadState.Error -> holder.showLoadingFailed()
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
                binding.lytLoadingFailed.btnDetails.setOnClickListener {
                    onDetailsClick(loadState as LoadState.Error)
                }
                binding.lytLoadingFailed.btnRetry.setOnClickListener { onRetryClick() }
                FooterViewHolder(binding)
            }
    }

    class FooterViewHolder (
        private val binding: ItemListFooterBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun showLoading() {
            binding.lytLoading.root.isVisible = true
            binding.lytLoadingFailed.root.isVisible = false
        }

        fun showLoadingFailed() {
            binding.lytLoading.root.isVisible = false
            binding.lytLoadingFailed.root.isVisible = true
        }

        fun hideAll() {
            binding.lytLoading.root.isVisible = false
            binding.lytLoadingFailed.root.isVisible = false
        }

    }

}