package com.example.otchallenge.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.example.otchallenge.R
import com.example.otchallenge.data.model.Book
import com.example.otchallenge.databinding.FragmentBookListBinding
import kotlinx.coroutines.launch

class BookListFragment : Fragment() {

    private lateinit var binding: FragmentBookListBinding
    private val bookListAdapter = BookListAdapter()

    private val dummyBooks = listOf(
        Book(
            name = "THE WOMAN",
            author = "Kristin Hannah",
            description = "In 1965, a nursing student follows her brother to serve during the Vietnam War and returns to a divided America.",
            imageUrl = "https://storage.googleapis.com/du-prd/books/images/9781250178633.jpg"
        ),
        Book(
            name = "SWAN SONG",
            author = "Elin Hilderbrand",
            description = "Nantucket residents are alarmed when a home, recently sold at an exorbitant price, goes up in flames and someone goes missing.",
            imageUrl = "https://storage.googleapis.com/du-prd/books/images/9780316258876.jpg"
        ),
        Book(
            name = "THE GOD OF THE WOODS",
            author = "Liz Moore",
            description = "When a 13-year-old girl disappears from an Adirondack summer camp in 1975, secrets kept by the Van Laar family emerge.",
            imageUrl = "https://storage.googleapis.com/du-prd/books/images/9780593418918.jpg"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Deprecated but necessary to retain Presenter state
        retainInstance = true
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookListAdapter.submitData(
                    pagingData = PagingData.from(dummyBooks)
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bookList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            )
            adapter = bookListAdapter
        }
    }

}