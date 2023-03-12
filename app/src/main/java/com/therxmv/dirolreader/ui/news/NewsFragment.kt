package com.therxmv.dirolreader.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.therxmv.dirolreader.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NewsFragment : Fragment() {
    private var binding: FragmentNewsBinding? = null
    private val vm: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater)
        vm.getClient()

        val recyclerView = binding?.recycleView
        val swipeRefreshLayout = binding?.swipeRefreshLayout

        val newsAdapter = NewsListAdapter(vm::updateMessage)
        val newsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerView?.apply {
            layoutManager = newsLayoutManager
            adapter = newsAdapter
        }

        swipeRefreshLayout?.setOnRefreshListener {
            vm.loadChannelsFromTdLib()
            swipeRefreshLayout.isRefreshing = false
            newsLayoutManager.scrollToPositionWithOffset(0, 0)
        }

        lifecycleScope.launchWhenCreated {
            vm.pagingData.collectLatest {
                newsAdapter.submitData(it)
            }
        }

        return binding!!.root
    }
}