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
import com.taipeizoo.remote.dto.response.BaseResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.net.URL

class MainPageViewModel(application: Application) : AndroidViewModel(application) {
    val isViewLoading = MutableLiveData<Boolean>()
    val downloadCompleted = MutableLiveData<BaseResponse>()
    val pagingDownloadCompleted = MutableLiveData<BaseResponse>()
    val downloadFailed = MutableLiveData<Boolean>()
    val downloadItemCompleted = MutableLiveData<Boolean>()
    val clickedCardView = MutableLiveData<AnimalAreaInfo>()

    fun onClickCardView(animalAreaInfo: AnimalAreaInfo) {
        clickedCardView.postValue(animalAreaInfo)
    }

    fun downloadAnimalInfo(start: Int, itemCountPerPage: Int, isPagingDownload: Boolean) {
        Log.e("", "Downloading.. ")
        isViewLoading.postValue(true)
        RetrofitClient.create(GeneralConstants.CONNECTION_URL)
                .create(GetService::class.java)
                .getAnimalArea("resourceAquire", itemCountPerPage, start)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it?.animalResponseDetail == null) {
                                Log.e("", "Download Failed ")
                                downloadFailed.postValue(true)
                            } else {
                                Log.e("", "Download Finished size: "
                                        + it.animalResponseDetail!!.animalAreaInfoList!!.size)
                                if (isPagingDownload) {
                                    pagingDownloadCompleted.postValue(it)
                                } else {
                                    downloadCompleted.postValue(it)
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
                                downloadItemCompleted.postValue(true)
                            },
                            onError = {}
                    )
        }
    }
}