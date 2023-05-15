package com.udemycourses.myplaces.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.udemycourses.myplaces.models.MyPlaceModel
import kotlinx.coroutines.selects.select

class DatabaseHandler (context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1 //Database version
        private const val DATABASE_NAME = "MyPlacesDatabase" // Database Name
        private const val TABLE_MY_PLACE = "MyPlacesTable" // Table Name

        //All the Columns names
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        //Create table with fields
        val CREATE_MY_PLACES_TABLE = ("CREATE TABLE " + TABLE_MY_PLACE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_MY_PLACES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_MY_PLACE")
        onCreate(db)
    }

    fun addMyPlace(myPlace : MyPlaceModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE,myPlace.title)
        contentValues.put(KEY_IMAGE,myPlace.image)
        contentValues.put(KEY_DESCRIPTION,myPlace.description)
        contentValues.put(KEY_DATE, myPlace.date)
        contentValues.put(KEY_LOCATION,myPlace.location)
        contentValues.put(KEY_LATITUDE, myPlace.latitude)
        contentValues.put(KEY_LONGITUDE,myPlace.longitude)

        //Inserting Row
        val result = db.insert(TABLE_MY_PLACE, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close()
        return result
    }
    fun updateMyPlace(myPlace : MyPlaceModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE,myPlace.title)
        contentValues.put(KEY_IMAGE,myPlace.image)
        contentValues.put(KEY_DESCRIPTION,myPlace.description)
        contentValues.put(KEY_DATE, myPlace.date)
        contentValues.put(KEY_LOCATION,myPlace.location)
        contentValues.put(KEY_LATITUDE, myPlace.latitude)
        contentValues.put(KEY_LONGITUDE,myPlace.longitude)

        val success = db.update(TABLE_MY_PLACE, contentValues, KEY_ID + "=" + myPlace.id, null)

        db.close()
        return success
    }

    fun deleteMyPlace(myPlace: MyPlaceModel): Int{
        val db = this.writableDatabase
        val success = db.delete(TABLE_MY_PLACE, KEY_ID + "=" + myPlace.id, null)

        db.close()
        return success
    }

    fun getMyPlacesList(): ArrayList<MyPlaceModel>{
        val myPlacesList : ArrayList<MyPlaceModel> = ArrayList<MyPlaceModel>()
        val selectQuery = "SELECT * FROM $TABLE_MY_PLACE"
        val db = this.readableDatabase

        try {
            val cursor : Cursor = db.rawQuery(selectQuery, null)

            if(cursor.moveToFirst()){
                do{
                    val place = MyPlaceModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE))
                    )
                    myPlacesList.add(place)

                }while (cursor.moveToNext())
            }
            cursor.close()

        }catch(e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return myPlacesList
    }

    //END
}
//END