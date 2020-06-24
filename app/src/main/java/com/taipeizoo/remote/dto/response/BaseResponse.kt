package com.taipeizoo.remote.dto.response

import com.google.gson.annotations.SerializedName

data class BaseResponse(
        @SerializedName("result") val animalResponseDetail: AnimalResponseDetail
)