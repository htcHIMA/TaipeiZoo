package com.taipeizoo.remote.dto.response

import com.google.gson.annotations.SerializedName

class PlantResponseDetail(
        @SerializedName("limit") val limit: Int,

        @SerializedName("offset") val offset: Int,

        @SerializedName("count") val count: Int,

        @SerializedName("sort") val sort: String,

        @SerializedName("results") val plantInfoList: List<PlantInfo>

)