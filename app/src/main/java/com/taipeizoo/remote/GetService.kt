package com.taipeizoo.remote

import com.taipeizoo.constnats.GeneralConstants
import com.taipeizoo.remote.dto.response.BasePlantResponse
import com.taipeizoo.remote.dto.response.BaseResponse
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface GetService {
    @GET(GeneralConstants.ANIMAL_ENDPOINT)
    fun getAnimalArea(@Query("scope") resourceAquire: String
                      , @Query("limit") limit: Int, @Query("offset") offset: Int): Single<BaseResponse>

    @GET(GeneralConstants.PLANT_ENDPOINT)
    fun getPlantInfo(@Query("scope") resourceAquire: String
                     , @Query("limit") limit: Int, @Query("offset") offset: Int
                     , @Query("q") keyword: String): Single<BasePlantResponse>

    @GET
    fun getImage(@Url url: String): Single<ResponseBody>
}