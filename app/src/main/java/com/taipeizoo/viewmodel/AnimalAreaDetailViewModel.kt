package com.taipeizoo.viewmodel

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.taipeizoo.constnats.GeneralConstants
import com.taipeizoo.remote.GetService
import com.taipeizoo.remote.dto.RetrofitClient
import com.taipeizoo.remote.dto.response.AnimalAreaInfo
import com.taipeizoo.remote.dto.response.BasePlantResponse
import com.taipeizoo.remote.dto.response.PlantInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.net.URL

class AnimalAreaDetailViewModel(application: Application) : AndroidViewModel(application) {
    val isViewLoading = MutableLiveData<Boolean>()
    val textCompleted = MutableLiveData<BasePlantResponse>()
    val pagingTextCompleted = MutableLiveData<BasePlantResponse>()
    val imageCompleted = MutableLiveData<Boolean>()
    val animalInfoImageCompleted = MutableLiveData<Boolean>();
    val downloadFailed = MutableLiveData<Boolean>()
    val clickedCardView = MutableLiveData<PlantInfo>()



    fun downloadPlantInfo(start: Int, itemCountPerPage: Int, animalAreaInfo: AnimalAreaInfo
                          , isPagingDownload: Boolean) {
        Log.e("", "Downloading.. ")
        isViewLoading.postValue(true)
        RetrofitClient.create(GeneralConstants.CONNECTION_URL)
                .create(GetService::class.java)
                .getPlantInfo("resourceAquire", itemCountPerPage, start, animalAreaInfo.e_Name!!)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it?.plantResponseDetail == null) {
                                Log.e("", "Download Failed ")
                                downloadFailed.postValue(true)
                            } else {
                                Log.e("", "Download Finished size: "
                                        + it.plantResponseDetail!!.plantInfoList!!.size)
                                if (isPagingDownload) {
                                    pagingTextCompleted.postValue(it)
                                } else {
                                    textCompleted.postValue(it)
                                }
                            }
                            isViewLoading.postValue(false)
                        },
                        onError = {
                            Log.e("", "ERROR Downloading");
                            isViewLoading.postValue(false)
                        }
                )

    }

    fun downloadImage(plantInfo: PlantInfo) {
        if (plantInfo.F_Pic01_URL!!.isNotEmpty()) {
            val fullURL = URL(plantInfo.F_Pic01_URL)
            val baseURL = fullURL.protocol + "://" + fullURL.host
            var fullEndPointURL = plantInfo.F_Pic01_URL.replace(baseURL, "")
            Log.e("download", "Downloading image: $fullEndPointURL")
            isViewLoading.postValue(true)
            RetrofitClient.create(baseURL)
                    .create(GetService::class.java)
                    .getImage(fullEndPointURL)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onSuccess = {
                                plantInfo.image = BitmapFactory.decodeStream(it.byteStream())
                                imageCompleted.postValue(true)
                            },
                            onError = {
                                downloadFailed.postValue(true)
                            }
                    )
        }
    }


    fun downloadImage(animalAreaInfo: AnimalAreaInfo) {
        if (animalAreaInfo.e_Pic_URL!!.isNotEmpty()) {
            val fullURL = URL(animalAreaInfo.e_Pic_URL)
            val baseURL = fullURL.protocol + "://" + fullURL.host
            val fullEndPointURL = animalAreaInfo.e_Pic_URL!!.replace(baseURL, "")
            if (!animalAreaInfo.e_Pic_URL!!.endsWith("/")) {
                animalAreaInfo.e_Pic_URL = animalAreaInfo.e_Pic_URL + "/"
            }
            Log.e("download", "Downloading image: " + animalAreaInfo.e_Pic_URL)
            isViewLoading.postValue(true)
            RetrofitClient.create(baseURL)
                    .create(GetService::class.java)
                    .getImage(fullEndPointURL)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onSuccess = {
                                animalAreaInfo.image = BitmapFactory.decodeStream(it.byteStream())
                                animalInfoImageCompleted.postValue(true)
                            },
                            onError = {}
                    )
        }
    }

    fun onClickCardView(plantInfo: PlantInfo) {
        clickedCardView.postValue(plantInfo)
    }
}