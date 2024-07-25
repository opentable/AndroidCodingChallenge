package com.example.otchallenge.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.otchallenge.R
import com.example.otchallenge.databinding.ActivityMainBinding
import com.example.otchallenge.domain.entity.ItemBook
import com.example.otchallenge.presentation.home.BooksAdapter
import com.example.otchallenge.presentation.home.HomeViewModel
import com.example.otchallenge.presentation.home.HomeViewModelImpl
import com.example.otchallenge.presentation.home.ViewTypeList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	private val binding: ActivityMainBinding by lazy {
		ActivityMainBinding.inflate(layoutInflater)
	}

	private val homeViewModel: HomeViewModel by viewModels<HomeViewModelImpl>()

	private val booksAdapter: BooksAdapter by lazy { BooksAdapter() }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(binding.root)
		setUpInsets()
		setUpUI()
		setUpListeners()
		setUpObservers()

		homeViewModel.getBooks()
	}

	private fun setUpUI() {
		binding.rcBooks.adapter = booksAdapter
	}

	private fun setUpListeners() {
		binding.toolbar.setOnMenuItemClickListener { menuItem ->
			when (menuItem.itemId) {
				R.id.item_view_type -> {
					toggleListViewType()
					true
				}
				else -> false
			}
		}
	}

	private fun setUpObservers() {
		homeViewModel.viewTypeListState.observe(this, ::updateMenuItem)
		homeViewModel.bookList.observe(this, ::updateBookList)
	}

	private fun updateMenuItem(state: ViewTypeList) {
		val icon = when (state) {
			ViewTypeList.VERTICAL -> R.drawable.ic_view_list
			ViewTypeList.CAROUSEL -> R.drawable.ic_view_carousel
		}
		val itemMenu = binding.toolbar.menu.findItem(R.id.item_view_type)
		itemMenu.setIcon(icon)
	}

	private fun updateBookList(itemBooks: List<ItemBook>) {
		booksAdapter.updateData(itemBooks)
	}

	private fun toggleListViewType() {
		val currentState = homeViewModel.viewTypeListState.value ?: ViewTypeList.VERTICAL
		homeViewModel.toggleViewTypeList(currentState)
	}

	private fun setUpInsets() {
		ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}
	}
}
