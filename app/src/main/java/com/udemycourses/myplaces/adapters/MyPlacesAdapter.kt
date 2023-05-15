package com.udemycourses.myplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.udemycourses.myplaces.R
import com.udemycourses.myplaces.activities.AddPlaceActivity
import com.udemycourses.myplaces.activities.MainActivity
import com.udemycourses.myplaces.databinding.ItemMyPlaceBinding
import com.udemycourses.myplaces.models.MyPlaceModel

open class MyPlacesAdapter (
    private val context: Context,
    private var list: ArrayList<MyPlaceModel>,
    private var onClickListener : OnClickListener? = null
        ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){


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

            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    onClickListener!!.onClick(position,model)
                }
            }
        }
    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int){
        val intent = Intent(context, AddPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS,list[position])
        activity.startActivityForResult(intent,requestCode)
        notifyItemChanged(position)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener{
        fun onClick(position: Int, model: MyPlaceModel)
    }

    inner class ViewHolder ( binding : ItemMyPlaceBinding ) :
        RecyclerView.ViewHolder ( binding.root ) {
        val tvTitle = binding.tvTitle
        val tvDescription = binding.tvDescription
        val civPlaceImage = binding.ivPlaceImage
    }

}