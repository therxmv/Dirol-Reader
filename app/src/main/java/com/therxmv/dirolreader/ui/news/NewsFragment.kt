package com.therxmv.dirolreader.ui.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.therxmv.dirolreader.databinding.FragmentNewsBinding
import com.therxmv.dirolreader.domain.models.ChannelModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment() {
    private var binding: FragmentNewsBinding? = null
    private var newsAdapter: NewsListAdapter? = null
    private val vm: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater)
        newsAdapter = NewsListAdapter()

        binding?.recycleView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = newsAdapter
        }

        vm.getClient()

        lifecycleScope.launchWhenCreated {
            vm.pagingData.collectLatest {
                newsAdapter?.submitData(it)
            }
        }

        return binding!!.root
    }
}