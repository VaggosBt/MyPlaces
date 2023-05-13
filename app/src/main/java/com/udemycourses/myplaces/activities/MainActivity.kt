package com.udemycourses.myplaces.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.udemycourses.myplaces.adapters.MyPlacesAdapter
import com.udemycourses.myplaces.database.DatabaseHandler
import com.udemycourses.myplaces.databinding.ActivityMainBinding
import com.udemycourses.myplaces.models.MyPlaceModel

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
        getMyPlacesListFromLocalDB()

    }

    private fun setupMyPlacesRecyclerView(myPlaceList : ArrayList<MyPlaceModel>){

        binding?.rvMyPlacesList?.layoutManager = LinearLayoutManager(this)

        binding?.rvMyPlacesList?.setHasFixedSize(true)

        val placesAdapter = MyPlacesAdapter(this,myPlaceList)

        binding?.rvMyPlacesList?.adapter = placesAdapter
    }

    private fun getMyPlacesListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getMyPlacesList : ArrayList<MyPlaceModel>  = dbHandler.getMyPlacesList()

        if(getMyPlacesList.size > 0 ){
            for(i in getMyPlacesList){

                binding?.rvMyPlacesList?.visibility = View.VISIBLE
                binding?.tvNoRecordsAvailable?.visibility = View.GONE

                setupMyPlacesRecyclerView(getMyPlacesList)

                Log.e("Title", i.title)
                Log.e("Description", i.description)
            }
        }else{
            binding?.rvMyPlacesList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }
}