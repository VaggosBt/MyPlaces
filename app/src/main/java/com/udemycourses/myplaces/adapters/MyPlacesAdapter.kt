package com.udemycourses.myplaces.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udemycourses.myplaces.R
import com.udemycourses.myplaces.databinding.ItemMyPlaceBinding
import com.udemycourses.myplaces.models.MyPlaceModel

open class MyPlacesAdapter (
    private val context: Context,
    private var list: ArrayList<MyPlaceModel>
        ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    inner class ViewHolder ( binding : ItemMyPlaceBinding ) :
        RecyclerView.ViewHolder ( binding.root ) {
        val tvTitle = binding.tvTitle
        val tvDescription = binding.tvDescription
        val civPlaceImage = binding.ivPlaceImage
    }

    override fun onCreateViewHolder ( parent : ViewGroup, viewType : Int ) : ViewHolder {
        return ViewHolder ( ItemMyPlaceBinding.inflate (
            LayoutInflater.from ( parent.context ), parent, false
        )
        )
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is ViewHolder ){
            holder.civPlaceImage.setImageURI(Uri.parse(model.image))
            holder.tvTitle.text = model.title
            holder.tvDescription.text = model.description
        }
    }

}