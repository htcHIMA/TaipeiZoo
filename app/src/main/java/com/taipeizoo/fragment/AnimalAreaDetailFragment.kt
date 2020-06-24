package com.taipeizoo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taipeizoo.R
import com.taipeizoo.activity.BaseActivity
import com.taipeizoo.activity.MainPageActivity
import com.taipeizoo.adapter.PlantInfoAdapter
import com.taipeizoo.constnats.GeneralConstants
import com.taipeizoo.databinding.FragAnimalAreaDetailBinding
import com.taipeizoo.remote.dto.response.AnimalAreaInfo
import com.taipeizoo.remote.dto.response.BasePlantResponse
import com.taipeizoo.remote.dto.response.PlantInfo
import com.taipeizoo.viewmodel.AnimalAreaDetailViewModel


class AnimalAreaDetailFragment : Fragment() {
    private lateinit var viewModel: AnimalAreaDetailViewModel
    private lateinit var plantInfoAdapter: PlantInfoAdapter
    private lateinit var animalAreaInfo: AnimalAreaInfo
    private lateinit var binding: FragAnimalAreaDetailBinding
    private var isScrolling = false
    private var currentItems = 0
    private var totalItems = 0
    private var scrollOutItems = 0
    private var isDownloadingImage = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.frag_animal_area_detail,
                container,
                false
        )
        binding.lifecycleOwner = this

        setupViewModel(binding)
        initData()
        val manager = LinearLayoutManager(context)
        binding.rvPlantInfo.layoutManager = manager
        binding.rvPlantInfo.setHasFixedSize(true)
        plantInfoAdapter = PlantInfoAdapter(context!!, viewModel)
        binding.rvPlantInfo.adapter = plantInfoAdapter
        binding.rvPlantInfo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                    viewModel.downloadPlantInfo(totalItems, GeneralConstants.ITEM_COUNT_PER_PAGE
                            , animalAreaInfo, true)
                    if (animalAreaInfo.image == null) {
                        viewModel.downloadImage(animalAreaInfo)
                    }
                }
            }
        })

        binding.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight &&
                        scrollY > oldScrollY) {
                    Log.e("refresh", "refresh")
                    currentItems = manager.childCount
                    totalItems = manager.itemCount
                    scrollOutItems = manager.findFirstVisibleItemPosition()
                    if (isScrolling && currentItems + scrollOutItems == totalItems) {
                        //Fetch Data
                        isScrolling = false
                        viewModel.isViewLoading.postValue(true)
                        viewModel.downloadPlantInfo(totalItems, GeneralConstants.ITEM_COUNT_PER_PAGE
                                , animalAreaInfo, true)
                    }
                }
            }
        }

        binding.appBar.setNavigationOnClickListener {
            fragmentManager!!.popBackStack()
        }

        viewModel.downloadPlantInfo(0, GeneralConstants.ITEM_COUNT_PER_PAGE, animalAreaInfo, false)

        return binding.root
    }

    private fun initData() {
        if (arguments != null && arguments!!.containsKey(GeneralConstants.ANIMAL_AREA_INFO)) {
            animalAreaInfo = arguments!!.getParcelable(GeneralConstants.ANIMAL_AREA_INFO)!!

            binding.ivAnimalArea.setImageBitmap(animalAreaInfo.image)
            binding.tvTitle.text = animalAreaInfo.e_Name
            binding.tvSubtitle.text = animalAreaInfo.e_Category
            binding.tvSupporting.text = animalAreaInfo.e_Info
        }
    }

    private fun setupViewModel(binding: FragAnimalAreaDetailBinding) {
        viewModel = ViewModelProviders.of(activity!!).get(AnimalAreaDetailViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.isViewLoading.observe(this, isViewLoadingObserver)
        viewModel.textCompleted.observe(this, textCompletedObserver)
        viewModel.pagingTextCompleted.observe(this, pagingTextCompletedObserver)
        viewModel.imageCompleted.observe(this, imageCompletedObserver)
        viewModel.clickedCardView.observe(this, clickedCardViewObserver)
        viewModel.downloadFailed.observe(this, downloadFailedObserver)
        viewModel.animalInfoImageCompleted.observe(this, animalInfoImageCompletedObserver)
    }

    private val animalInfoImageCompletedObserver = Observer<Boolean> {
        if (it) {
            binding.ivAnimalArea.setImageBitmap(animalAreaInfo.image)
        }
        viewModel.downloadFailed.postValue(false)
    }

    private val downloadFailedObserver = Observer<Boolean> {
        if (it) {
            Toast.makeText(context, "下載失敗", Toast.LENGTH_SHORT).show()
        }
        viewModel.downloadFailed.postValue(false)
    }

    private val clickedCardViewObserver = Observer<PlantInfo> {
        if (it != null) {
            Log.e("onClicked", "open card : " + it.F_Name_Ch)
            val bundle = Bundle()
            bundle.putParcelable(GeneralConstants.PLANT_INFO, it)
            val plantDetailFragment = PlantDetailFragment()
            plantDetailFragment.arguments = bundle
            (activity as BaseActivity).navigateTo(plantDetailFragment, true)
        }
        viewModel.clickedCardView.postValue(null)
    }

    private val imageCompletedObserver = Observer<Boolean> {
        if (it) {
            plantInfoAdapter.notifyDataSetChanged()
        }
        viewModel.imageCompleted.postValue(false)
        viewModel.isViewLoading.postValue(false)
    }

    private val pagingTextCompletedObserver = Observer<BasePlantResponse> {
        if (it?.plantResponseDetail != null && !isDownloadingImage) {
            isDownloadingImage = true
            for (animalAreaInfo in it.plantResponseDetail.plantInfoList) {
                viewModel.downloadImage(animalAreaInfo)
            }

            plantInfoAdapter.allPlantInfoInfo
                    .addAll((it.plantResponseDetail.plantInfoList as ArrayList<PlantInfo>))
            plantInfoAdapter.notifyDataSetChanged()
            isDownloadingImage = false
            viewModel.pagingTextCompleted.postValue(null)
            viewModel.isViewLoading.postValue(false)
        }
    }
    private val textCompletedObserver = Observer<BasePlantResponse> {
        if (it?.plantResponseDetail != null) {
            for (animalAreaInfo in it.plantResponseDetail.plantInfoList) {
                viewModel.downloadImage(animalAreaInfo)
            }
            plantInfoAdapter.refresh(it.plantResponseDetail.plantInfoList as ArrayList<PlantInfo>)

            viewModel.textCompleted.postValue(null)
            viewModel.isViewLoading.postValue(false)
        }
    }
    private val isViewLoadingObserver: Observer<Boolean> = Observer {
        if (activity is MainPageActivity) {
            (activity as MainPageActivity?)!!.showProgress(it!!)
        }
    }
}