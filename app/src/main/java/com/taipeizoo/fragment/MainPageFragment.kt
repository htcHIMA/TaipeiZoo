package com.taipeizoo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taipeizoo.R
import com.taipeizoo.activity.BaseActivity
import com.taipeizoo.activity.MainPageActivity
import com.taipeizoo.adapter.AnimalAreaInfoAdapter
import com.taipeizoo.constnats.GeneralConstants
import com.taipeizoo.databinding.FragMainPageBinding
import com.taipeizoo.remote.dto.response.AnimalAreaInfo
import com.taipeizoo.remote.dto.response.BaseResponse
import com.taipeizoo.viewmodel.MainPageViewModel

class MainPageFragment : Fragment() {
    private lateinit var viewModel: MainPageViewModel
    private lateinit var animalAreaInfoAdapter: AnimalAreaInfoAdapter
    private var isScrolling = false
    private var currentItems = 0
    private var totalItems = 0
    private var scrollOutItems = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragMainPageBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.frag_main_page,
                container,
                false
        )
        binding.lifecycleOwner = this
        setupViewModel(binding)
        setupRecyclerView(binding)
        viewModel.downloadAnimalInfo(0, GeneralConstants.ITEM_COUNT_PER_PAGE, false)
        return binding.root
    }

    private fun setupRecyclerView(binding: FragMainPageBinding) {
        val manager = LinearLayoutManager(context)
        binding.rvAnimalArea.layoutManager = manager
        binding.rvAnimalArea.setHasFixedSize(true)
        animalAreaInfoAdapter = AnimalAreaInfoAdapter(context!!, viewModel)
        binding.rvAnimalArea.adapter = animalAreaInfoAdapter
        binding.rvAnimalArea.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = manager.childCount
                totalItems = manager.itemCount
                scrollOutItems = manager.findFirstVisibleItemPosition()
                if (isScrolling && currentItems + scrollOutItems == totalItems) {
                    //Fetch Data
                    isScrolling = false
                    viewModel.isViewLoading.postValue(true)
                    viewModel.downloadAnimalInfo(totalItems, GeneralConstants.ITEM_COUNT_PER_PAGE, true)
                }
            }
        })
    }

    private fun setupViewModel(binding: FragMainPageBinding) {
        viewModel = ViewModelProviders.of(activity!!).get(MainPageViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.isViewLoading.observe(this, isViewLoadingObserver)
        viewModel.downloadCompleted.observe(this, downloadCompletedObserver)
        viewModel.pagingDownloadCompleted.observe(this, pagingDownloadCompletedObserver)
        viewModel.downloadItemCompleted.observe(this, downloadItemCompletedObserver)
        viewModel.clickedCardView.observe(this, clickedCardViewObserver)
    }

    private val clickedCardViewObserver = Observer<AnimalAreaInfo> {
        if (it != null) {
            Log.e("onClicked", "open card : " + it.e_Name)
            val bundle = Bundle()
            bundle.putParcelable(GeneralConstants.ANIMAL_AREA_INFO, it)
            val animalAreaDetailFragment = AnimalAreaDetailFragment()
            animalAreaDetailFragment.arguments = bundle
            (activity as BaseActivity).navigateTo(animalAreaDetailFragment, true)
        }
        viewModel.clickedCardView.postValue(null)
    }

    private val downloadItemCompletedObserver = Observer<Boolean> {
        if (it) {
            animalAreaInfoAdapter.notifyDataSetChanged()
        }
        viewModel.isViewLoading.postValue(false)
        viewModel.downloadItemCompleted.postValue(false)
    }

    private val pagingDownloadCompletedObserver = Observer<BaseResponse> {
        viewModel.isViewLoading.postValue(false)
        if (it?.animalResponseDetail != null) {
            for (animalAreaInfo in it.animalResponseDetail.animalAreaInfoList) {
                viewModel.downloadImage(animalAreaInfo)
            }

            animalAreaInfoAdapter.allAnimalAreaInfo
                    .addAll((it.animalResponseDetail.animalAreaInfoList as ArrayList<AnimalAreaInfo>))
            animalAreaInfoAdapter.notifyDataSetChanged()
            viewModel.pagingDownloadCompleted.postValue(null)
        }
    }
    private val downloadCompletedObserver = Observer<BaseResponse> {
        if (it?.animalResponseDetail != null) {
            for (animalAreaInfo in it.animalResponseDetail.animalAreaInfoList) {
                viewModel.downloadImage(animalAreaInfo)
            }
            animalAreaInfoAdapter.refresh(it.animalResponseDetail.animalAreaInfoList as ArrayList<AnimalAreaInfo>)
        }
        viewModel.downloadCompleted.postValue(null)
        viewModel.isViewLoading.postValue(false)
    }
    private val isViewLoadingObserver: Observer<Boolean> = Observer {
        if (activity is MainPageActivity) {
            (activity as MainPageActivity?)!!.showProgress(it!!)
        }
    }
}