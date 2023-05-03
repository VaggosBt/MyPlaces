package com.udemycourses.myplaces

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.udemycourses.myplaces.databinding.ActivityAddPlaceBinding

class AddPlaceActivity : AppCompatActivity() {

    private var binding : ActivityAddPlaceBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarAddPlace)
       supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarAddPlace?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}