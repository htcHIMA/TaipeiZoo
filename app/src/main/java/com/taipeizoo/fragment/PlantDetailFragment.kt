package com.taipeizoo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.taipeizoo.R
import com.taipeizoo.activity.MainPageActivity
import com.taipeizoo.constnats.GeneralConstants
import com.taipeizoo.databinding.FragPlantDetailBinding
import com.taipeizoo.remote.dto.response.BasePlantResponse
import com.taipeizoo.remote.dto.response.PlantInfo
import com.taipeizoo.viewmodel.PlantDetailViewModel

class PlantDetailFragment : Fragment() {
    private lateinit var viewModel: PlantDetailViewModel
    private lateinit var binding: FragPlantDetailBinding
    private lateinit var plantInfo: PlantInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.frag_plant_detail,
                container,
                false
        )
        binding.lifecycleOwner = this

        setupViewModel(binding)
        initData()
        viewModel.downloadPlantInfo(plantInfo.F_Name_Ch!!)

        binding.appBar.setNavigationOnClickListener {
            fragmentManager!!.popBackStack()
        }

        return binding.root
    }

    private fun initData() {
        if (arguments != null && arguments!!.containsKey(GeneralConstants.PLANT_INFO)) {
            plantInfo = arguments!!.getParcelable(GeneralConstants.PLANT_INFO)!!
        }
    }


    private fun setupViewModel(binding: FragPlantDetailBinding) {
        viewModel = ViewModelProviders.of(activity!!).get(PlantDetailViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.isViewLoading.observe(this, isViewLoadingObserver)
        viewModel.textCompleted.observe(this, textCompletedObserver)
        viewModel.imageCompleted.observe(this, imageCompletedObserver)
    }

    private val imageCompletedObserver = Observer<Boolean> {
        if (it) {
            binding.ivPlantImage.setImageBitmap(plantInfo.image)
        }
        viewModel.imageCompleted.postValue(false)
        viewModel.isViewLoading.postValue(false)
    }

    private val textCompletedObserver = Observer<BasePlantResponse> {
        if (it?.plantResponseDetail != null && it.plantResponseDetail.plantInfoList.isNotEmpty()) {
            plantInfo = it.plantResponseDetail.plantInfoList[0]
            viewModel.downloadImage(plantInfo)
            viewModel.initPlantInfoText(plantInfo)
        }
        viewModel.textCompleted.postValue(null)
        viewModel.isViewLoading.postValue(false)
    }
    private val isViewLoadingObserver: Observer<Boolean> = Observer {
        if (activity is MainPageActivity) {
            (activity as MainPageActivity?)!!.showProgress(it!!)
        }
    }
}