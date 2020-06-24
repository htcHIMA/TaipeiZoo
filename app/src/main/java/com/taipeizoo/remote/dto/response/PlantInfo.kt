package com.taipeizoo.remote.dto.response

import android.graphics.Bitmap
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlantInfo(
        @SerializedName("F_Name_Latin") var f_Name_Latin: String?,
        @SerializedName("F_pdf02_ALT") val F_pdf02_ALT: String?,
        @SerializedName("F_Location") val F_Location: String?,
        @SerializedName("F_pdf01_ALT") val F_pdf01_ALT: String?,
        @SerializedName("rank") val rank: Double?,
        @SerializedName("F_Summary") val F_Summary: String?,
        @SerializedName("F_Pic01_URL") val F_Pic01_URL: String?,
        @SerializedName("_full_count") val _full_count: String?,
        @SerializedName("F_pdf02_URL") val F_pdf02_URL: String?,
        @SerializedName("F_Pic02_URL") val F_Pic02_URL: String?,
        @SerializedName("F_Keywords") val F_Keywords: String?,
        @SerializedName("F_Code") val F_Code: String?,
        @SerializedName("F_Geo") val F_Geo: String?,
        @SerializedName("F_Pic03_URL") val F_Pic03_URL: String?,
        @SerializedName("F_Voice01_ALT") val F_Voice01_ALT: String?,
        @SerializedName("F_AlsoKnown") val F_AlsoKnown: String?,
        @SerializedName("F_Voice02_ALT") val F_Voice02_ALT: String?,
        @SerializedName("F_Name_Ch") val F_Name_Ch: String?,
        @SerializedName("F_Pic04_ALT") val F_Pic04_ALT: String?,
        @SerializedName("F_Name_En") val F_Name_En: String?,
        @SerializedName("F_Brief") val F_Brief: String?,
        @SerializedName("F_Pic04_URL") val F_Pic04_URL: String?,
        @SerializedName("F_Voice01_URL") val F_Voice01_URL: String?,
        @SerializedName("F_Feature") val F_Feature: String?,
        @SerializedName("F_Pic02_ALT") val F_Pic02_ALT: String?,
        @SerializedName("F_Family") val F_Family: String?,
        @SerializedName("F_Voice03_ALT") val F_Voice03_ALT: String?,
        @SerializedName("F_Voice02_URL") val F_Voice02_URL: String?,
        @SerializedName("F_Pic03_ALT") val F_Pic03_ALT: String?,
        @SerializedName("F_Pic01_ALT") val F_Pic01_ALT: String?,
        @SerializedName("F_CID") val F_CID: String?,
        @SerializedName("F_pdf01_URL") val F_pdf01_URL: String?,
        @SerializedName("F_Vedio_URL") val F_Vedio_URL: String?,
        @SerializedName("F_Genus") val F_Genus: String?,
        @SerializedName("F_Function＆Application") val F_FunctionApplication: String?,
        @SerializedName("F_Voice03_URL") val F_Voice03_URL: String?,
        @SerializedName("F_Update") val F_Update: String?,
        @SerializedName("_id") val _id: Int?,

        var image: Bitmap
) : Parcelable