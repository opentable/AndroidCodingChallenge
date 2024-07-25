package com.example.otchallenge.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.otchallenge.databinding.ItemBookVerticalBinding
import com.example.otchallenge.domain.entity.ItemBook

class BooksAdapter : Adapter<ItemBookVerticalHolder>() {
    private val dataSource: MutableList<ItemBook> = mutableListOf(
        ItemBook(
            title = "The Lord of the Rings",
            author = "J.R.R. Tolkien",
            description = "Epic high fantasy trilogy following the quest to destroy the One Ring.",
            bookImageUrl = "https://storage.googleapis.com/du-prd/books/images/9780063384200.jpg",
            amazonBuyUrl = "https://www.amazon.com/Lord-Rings-Fellowship-Ring/dp/0618057571",
            isbn = "978-0618057576"
        ),
        ItemBook(
            title = "Pride and Prejudice",
            author = "Jane Austen",
            description = "A witty social commentary on love and class in 19th century England.",
            bookImageUrl = "https://storage.googleapis.com/du-prd/books/images/9780063384200.jpg",
            amazonBuyUrl = "https://www.amazon.com/Lord-Rings-Fellowship-Ring/dp/0618057571",
            isbn = "978-0141439518"
        ),
        ItemBook(
            title = "To Kill a Mockingbird",
            author = "Harper Lee",
            description = "A coming-of-age story set in the American South exploring racial injustice.",
            bookImageUrl = "https://storage.googleapis.com/du-prd/books/images/9780063384200.jpg",
            amazonBuyUrl = "https://www.amazon.com/Kill-Mockingbird-Harper-Lee/dp/0446310786",
            isbn = "978-0446310789"
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBookVerticalHolder {
        val itemVertical =
            ItemBookVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemBookVerticalHolder(itemVertical)
    }

    override fun getItemCount(): Int = dataSource.size


    override fun onBindViewHolder(holder: ItemBookVerticalHolder, position: Int) {
        holder.bind(dataSource[position])
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