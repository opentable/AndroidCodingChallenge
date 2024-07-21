package com.example.otchallenge.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.otchallenge.MyApplication
import com.example.otchallenge.R
import com.example.otchallenge.presentation.model.BookDetailPresentation
import com.example.otchallenge.presentation.model.BookPresentation
import com.example.otchallenge.presentation.presenter.BookPresenterContract
import javax.inject.Inject

class BookDetailFragment : Fragment(), BookView {

    @Inject
    lateinit var presenter: BookPresenterContract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_detail, container, false)
        return view
    }

    override fun showBooks(books: List<BookPresentation>) {
        TODO("Not yet implemented")
    }

    override fun showBookDetails(book: BookDetailPresentation) {
        TODO("Not yet implemented")
    }

    override fun showError(error: String) {
        TODO("Not yet implemented")
    }
}