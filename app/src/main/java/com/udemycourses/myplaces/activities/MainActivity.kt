package com.udemycourses.myplaces.activities

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.udemycourses.myplaces.adapters.MyPlacesAdapter
import com.udemycourses.myplaces.database.DatabaseHandler
import com.udemycourses.myplaces.databinding.ActivityMainBinding
import com.udemycourses.myplaces.models.MyPlaceModel
import com.udemycourses.myplaces.utils.SwipeToEditCallback

class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding? = null



    override fun onCreate(savedInstanceState: Bundle?) {

        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                Log.e("START FOR RESULT", "START FOR RESULT ENTERED")
                if (result.resultCode == Activity.RESULT_OK) {
                    Log.e("RESULT CODE", result.resultCode.toString())
                    Log.e("RESULT CODE IF", (result.resultCode == Activity.RESULT_OK).toString())
                    getMyPlacesListFromLocalDB()
                } else {
                    Log.e("RESULT CODE", result.resultCode.toString())
                    Log.e("RESULT CODE IF", (result.resultCode == Activity.RESULT_OK).toString())
                    Log.e("Activity", "Cancelled or Back Pressed")
                }
            }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.fabAddMyPlace?.setOnClickListener{
            val intent = Intent (this, AddPlaceActivity::class.java)
            startForResult.launch(intent)
        }
        getMyPlacesListFromLocalDB()

    }

    private fun setupMyPlacesRecyclerView(myPlaceList : ArrayList<MyPlaceModel>){

        binding?.rvMyPlacesList?.layoutManager = LinearLayoutManager(this)

        binding?.rvMyPlacesList?.setHasFixedSize(true)

        val placesAdapter = MyPlacesAdapter(this,myPlaceList)

        binding?.rvMyPlacesList?.adapter = placesAdapter

        placesAdapter.setOnClickListener(object: MyPlacesAdapter.OnClickListener{

            override fun onClick(position: Int, model: MyPlaceModel) {

                val intent = Intent(this@MainActivity, MyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS,model)

                startActivity(intent)
            }
        })

        val editsSwipeHandler = object : SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding?.rvMyPlacesList?.adapter as MyPlacesAdapter
                adapter.notifyEditItem(this@MainActivity, viewHolder.adapterPosition, ADD_PLACE_ACTIVITY_REQUEST_CODE)
            }

        }

        val editItemTouchHelper  = ItemTouchHelper(editsSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding?.rvMyPlacesList)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("Activity" , "ON ACTIVITY RESULT CALLED")
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("Activity" , resultCode.toString())
        if(requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE){

            if(resultCode == Activity.RESULT_OK){
                getMyPlacesListFromLocalDB()
            }else{
                Log.e("Activity" , "Cancelled or Back Pressed")
            }
        }
    }

    companion object {
        var ADD_PLACE_ACTIVITY_REQUEST_CODE = 123
        var EXTRA_PLACE_DETAILS = "extra_place_details"


    }
}