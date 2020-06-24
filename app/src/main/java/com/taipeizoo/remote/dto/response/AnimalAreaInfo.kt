package com.taipeizoo.remote.dto.response

import android.graphics.Bitmap
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnimalAreaInfo(
        @SerializedName("E_Pic_URL") var e_Pic_URL: String?,

        @SerializedName("E_Geo") val e_Geo: String?,

        @SerializedName("E_Info") val e_Info: String?,

        @SerializedName("E_no") val e_no: String?,

        @SerializedName("E_Category") val e_Category: String?,

        @SerializedName("E_Name") val e_Name: String?,

        @SerializedName("E_Memo") val e_Memo: String?,

        @SerializedName("_id") val _id: Int,

        @SerializedName("E_URL") val e_URL: String?,

        var image: Bitmap?

) : Parcelable