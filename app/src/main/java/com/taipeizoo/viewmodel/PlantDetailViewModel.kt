package com.taipeizoo.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.taipeizoo.constnats.GeneralConstants
import com.taipeizoo.remote.GetService
import com.taipeizoo.remote.dto.RetrofitClient
import com.taipeizoo.remote.dto.response.BasePlantResponse
import com.taipeizoo.remote.dto.response.PlantInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.net.URL

class PlantDetailViewModel(application: Application) : AndroidViewModel(application) {
    val isViewLoading = MutableLiveData<Boolean>()
    val textCompleted = MutableLiveData<BasePlantResponse>()
    val imageCompleted = MutableLiveData<Boolean>()

    val chineseName = MutableLiveData<String>()
    val latinName = MutableLiveData<String>()
    val alsoKnown = MutableLiveData<String>()
    val feature = MutableLiveData<String>()
    val functionAndApplication = MutableLiveData<String>()
    val lastUpdate = MutableLiveData<String>()
    val brief = MutableLiveData<String>()
    val plantImage = MutableLiveData<Bitmap>()
    val downloadTextFailed = MutableLiveData<Boolean>()
    val downloadImageFailed = MutableLiveData<Boolean>()

    fun initPlantInfoText(plantInfo: PlantInfo) {
        chineseName.value = plantInfo.F_Name_Ch
        latinName.value = plantInfo.f_Name_Latin
        alsoKnown.value = plantInfo.F_AlsoKnown
        feature.value = plantInfo.F_Feature
        brief.value = plantInfo.F_Brief
        functionAndApplication.value = plantInfo.F_FunctionApplication
        lastUpdate.value = plantInfo.F_Update
    }

    fun downloadPlantInfo(plantName: String) {
        Log.e("", "Downloading.. ")
        isViewLoading.postValue(true)
        RetrofitClient.create(GeneralConstants.CONNECTION_URL)
                .create(GetService::class.java)
                .getPlantInfo("resourceAquire", 1, 0, plantName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it?.plantResponseDetail == null) {
                                Log.e("", "Download Failed ")
                                downloadTextFailed.postValue(true)
                            } else {
                                Log.e("", "Download Finished size: "
                                        + it.plantResponseDetail!!.plantInfoList!!.size)
                                textCompleted.postValue(it)
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
                                downloadImageFailed.postValue(true)
                            }
                    )
        }
    }
}