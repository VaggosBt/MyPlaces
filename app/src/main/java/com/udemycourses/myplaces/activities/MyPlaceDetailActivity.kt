package com.udemycourses.myplaces.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.udemycourses.myplaces.R
import com.udemycourses.myplaces.databinding.ActivityMyPlaceDetailBinding
import com.udemycourses.myplaces.models.MyPlaceModel

class MyPlaceDetailActivity : AppCompatActivity() {

    var binding : ActivityMyPlaceDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        var myPlaceDetailModel : MyPlaceModel? = null

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            myPlaceDetailModel = intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as MyPlaceModel

        }

        if(myPlaceDetailModel != null) {

            setSupportActionBar(binding?.toolbarMyPlaceDetail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = myPlaceDetailModel.title

            binding?.toolbarMyPlaceDetail?.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            binding?.ivPlaceImage?.setImageURI((Uri.parse(myPlaceDetailModel.image)))
            binding?.tvDescription?.text = myPlaceDetailModel.description
            binding?.tvLocation?.text = myPlaceDetailModel.location


        }

    }
}