package com.example.otchallenge.books.presentation

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.otchallenge.MyApplication
import com.example.otchallenge.R
import com.example.otchallenge.books.di.DaggerBooksComponent
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BooksActivity : AppCompatActivity(), BooksView {

	@Inject lateinit var booksPresenter: BookPresenter
	@Inject lateinit var glide: RequestManager

	private lateinit var mainView: ConstraintLayout
	private lateinit var lastModifiedTv: TextView
	private lateinit var progressBar: ProgressBar
	private lateinit var booksAdapter: BooksAdapter
	private val compositeDisposable: CompositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		DaggerBooksComponent
			.builder()
			.appComponent((application as MyApplication).appComponent)
			.booksView(this)
			.build()
			.inject(this)

		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_main)

		mainView = findViewById(R.id.main)
		ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		val toolbar = findViewById<Toolbar>(R.id.toolbar)
		setSupportActionBar(toolbar)

		val booksRv = findViewById<RecyclerView>(R.id.books_rv)
		lastModifiedTv = findViewById(R.id.last_modified_tv)
		progressBar = findViewById(R.id.progressbar)

		booksAdapter = BooksAdapter(glide)
		booksRv.adapter = booksAdapter
		compositeDisposable.add(booksPresenter.fetchBooksFromLocal())
		if (savedInstanceState == null) {
			compositeDisposable.add(booksPresenter.fetchBooksFromRemote(0))
		}
	}

	override fun showBooks(books: List<BookUiModel>) {
		booksAdapter.submitList(books)
	}

	override fun showLastModifiedDate(date: String) {
		lastModifiedTv.text = date
	}

	override fun showNoBooksFound() {
		Snackbar.make(mainView, getString(R.string.no_books_found), Snackbar.LENGTH_LONG).apply {
			show()
		}
	}

	override fun showTryAgainError(error: String) {
		Snackbar.make(mainView, error, Snackbar.LENGTH_LONG).apply {
			show()
			setAction(R.string.try_again) {
				compositeDisposable.add(booksPresenter.retryLastFetch())
			}
		}
	}

	override fun setLoading(isLoading: Boolean) {
		progressBar.isVisible = isLoading
	}

	override fun onDestroy() {
		super.onDestroy()
		compositeDisposable.clear()
	}
}
