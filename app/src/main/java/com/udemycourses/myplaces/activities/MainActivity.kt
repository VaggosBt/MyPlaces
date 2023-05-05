package com.udemycourses.myplaces.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.udemycourses.myplaces.activities.AddPlaceActivity
import com.udemycourses.myplaces.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.fabAddMyPlace?.setOnClickListener{
            val intent = Intent (this, AddPlaceActivity::class.java)
            startActivity(intent)
        }

    }
}