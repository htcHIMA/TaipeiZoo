package com.taipeizoo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.taipeizoo.R
import com.taipeizoo.adapter.PlantInfoAdapter.PlantInfoViewHolder
import com.taipeizoo.remote.dto.response.PlantInfo
import com.taipeizoo.viewmodel.AnimalAreaDetailViewModel
import java.util.*

class PlantInfoAdapter(private val mContext: Context, private val viewModel: AnimalAreaDetailViewModel)
    : RecyclerView.Adapter<PlantInfoViewHolder>() {
    var allPlantInfoInfo = ArrayList<PlantInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantInfoViewHolder {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.cell_animal_area_info, parent, false)

        return PlantInfoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return allPlantInfoInfo.size
    }

    override fun onBindViewHolder(holder: PlantInfoViewHolder, position: Int) {
        if (position < allPlantInfoInfo.size) {
            val plantInfo = allPlantInfoInfo[position]
            holder.tv_title.text = plantInfo.F_Name_Ch
            holder.tv_subtitle.text = plantInfo.F_Location
            holder.tv_supporting.text = plantInfo.F_AlsoKnown
            holder.iv_animal_area.setImageBitmap(plantInfo.image)

            holder.card.setOnClickListener {
                viewModel.onClickCardView(plantInfo)
            }
        }
    }


    fun refresh(plantInfoList: ArrayList<PlantInfo>) {
        allPlantInfoInfo = plantInfoList
        notifyDataSetChanged()
    }


    class PlantInfoViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {

        var tv_title: TextView = itemView.findViewById(R.id.tv_title)
        var tv_subtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        var tv_supporting: TextView = itemView.findViewById(R.id.tv_supporting)
        var iv_animal_area: ImageView = itemView.findViewById(R.id.iv_animal_area)
        var card: CardView = itemView.findViewById(R.id.card)

    }
}