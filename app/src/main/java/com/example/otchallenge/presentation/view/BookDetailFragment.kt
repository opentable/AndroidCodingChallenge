package com.example.otchallenge.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.otchallenge.MyApplication
import com.example.otchallenge.R
import com.example.otchallenge.presentation.model.BookDetailPresentation
import com.example.otchallenge.presentation.presenter.BookDetailPresenter
import com.example.otchallenge.utils.GlideApp
import javax.inject.Inject

class BookDetailFragment : Fragment(), BookDetailView {

    @Inject
    lateinit var presenter: BookDetailPresenter

    private lateinit var bookImage: ImageView
    private lateinit var bookTitle: TextView
    private lateinit var bookAuthor: TextView
    private lateinit var bookPrice: TextView
    private lateinit var bookDescription: TextView
    private lateinit var buyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as MyApplication).appComponent.inject(this)

        // Handle back press for all versions
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_detail, container, false)

        bookImage = view.findViewById(R.id.book_image)
        bookTitle = view.findViewById(R.id.book_title)
        bookAuthor = view.findViewById(R.id.book_author)
        bookPrice = view.findViewById(R.id.book_price)
        bookDescription = view.findViewById(R.id.book_description)
        buyButton = view.findViewById(R.id.buy_button)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        val bookId = arguments?.getInt("bookId") ?: return
        presenter.loadBookDetails(bookId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun showBookDetails(book: BookDetailPresentation) {
        bookTitle.text = book.title
        bookAuthor.text = book.author
        bookPrice.text = book.price
        bookDescription.text = book.description

        GlideApp.with(this)
            .load(book.bookImage)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(bookImage)

        buyButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(book.amazonProductUrl))
            startActivity(browserIntent)
        }
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}