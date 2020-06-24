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
import com.taipeizoo.adapter.AnimalAreaInfoAdapter.AnimalAreaInfoViewHolder
import com.taipeizoo.remote.dto.response.AnimalAreaInfo
import com.taipeizoo.viewmodel.MainPageViewModel
import java.util.*

class AnimalAreaInfoAdapter(private val mContext: Context, private val viewModel: MainPageViewModel)
    : RecyclerView.Adapter<AnimalAreaInfoViewHolder>() {
    var allAnimalAreaInfo = ArrayList<AnimalAreaInfo>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalAreaInfoViewHolder {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.cell_animal_area_info, parent, false)

        return AnimalAreaInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalAreaInfoViewHolder, position: Int) {
        if (position < allAnimalAreaInfo.size) {
            val animalAreaInfo = allAnimalAreaInfo[position]
            holder.tv_title.text = animalAreaInfo.e_Name
            holder.tv_subtitle.text = animalAreaInfo.e_Category
            holder.tv_supporting.text = animalAreaInfo.e_Info
            holder.iv_animal_area.setImageBitmap(animalAreaInfo.image)

            holder.card.setOnClickListener {
                viewModel.onClickCardView(animalAreaInfo)
            }
        }
    }

    override fun getItemCount(): Int {
        return allAnimalAreaInfo.size
    }

    fun refresh(areaInfoArrayList: ArrayList<AnimalAreaInfo>) {
        allAnimalAreaInfo = areaInfoArrayList
        notifyDataSetChanged()
    }

    class AnimalAreaInfoViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {

        var tv_title: TextView = itemView.findViewById(R.id.tv_title)
        var tv_subtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        var tv_supporting: TextView = itemView.findViewById(R.id.tv_supporting)
        var iv_animal_area: ImageView = itemView.findViewById(R.id.iv_animal_area)
        var card: CardView = itemView.findViewById(R.id.card)

    }

}