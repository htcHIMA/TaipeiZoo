package com.taipeizoo.remote.dto.response

import com.google.gson.annotations.SerializedName

data class BasePlantResponse(
        @SerializedName("result") val plantResponseDetail: PlantResponseDetail
)