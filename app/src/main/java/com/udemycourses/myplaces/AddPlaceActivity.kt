package com.udemycourses.myplaces

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RecordingCanvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.AndroidException
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.udemycourses.myplaces.databinding.ActivityAddPlaceBinding
import java.text.SimpleDateFormat
import java.util.*

class AddPlaceActivity : AppCompatActivity() , View.OnClickListener{

    private var binding: ActivityAddPlaceBinding? = null
    private val cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    companion object{
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                //Toast.makeText(this@AddPlaceActivity, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show()
                //binding?.ivImage?.setImageResource(result.resultCode)


                try{
                    val selectedImageUri = result.data?.data
                    val inputStream = selectedImageUri?.let { contentResolver.openInputStream(it) }
                    val imageBitmap = BitmapFactory.decodeStream(inputStream)
                    binding?.ivImage?.setImageBitmap(imageBitmap)
                }catch (e: java.lang.RuntimeException){
                    Toast.makeText(this@AddPlaceActivity,"Something went wrong", Toast.LENGTH_SHORT).show()
                }catch (t: Throwable){
                    Toast.makeText(this@AddPlaceActivity,"Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val requestPermission : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
                permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value

                if(isGranted){

                    Toast.makeText(this, "Permission granted, now you can read the storage files", Toast.LENGTH_SHORT).show()
                    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)
                }else{
                    if(permissionName == Manifest.permission.CAMERA){
                        Toast.makeText(this, "Oops, you just denied the permission.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, proceed with camera operation
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                // Permission denied, show rationale or disable camera functionality
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarAddPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarAddPlace?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        binding?.etDate?.setOnClickListener(this)
        binding?.tvAddImage?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            binding!!.etDate.id -> {
                DatePickerDialog(
                    this@AddPlaceActivity,
                    dateSetListener, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            binding!!.tvAddImage.id -> {

                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems =
                    arrayOf("Select photo from Gallery", "Capture photo from camera")
                pictureDialog.setItems(pictureDialogItems) { dialog, choice ->
                    when (choice) {

                        0 -> choosePhotoFromGallery()


                        1 -> capturePhotoFromCamera()
                    }
                }.show()
            }
        }
    }

    private fun choosePhotoFromGallery() {

        requestStoragePermission()

    }

    private fun capturePhotoFromCamera(){
        //Toast.makeText(this@AddPlaceActivity,"Camera selection coming soon...",Toast.LENGTH_SHORT).show()
        requestCameraPermission()
    }


    private fun updateDateInView() {
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding?.etDate?.setText(sdf.format(cal.time).toString())
    }

    private fun requestStoragePermission() {
        // for android Android 11 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                // Permission already granted
                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                openGalleryLauncher.launch(pickIntent)
            } else {
                // Permission not granted, request it
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
                Toast.makeText(
                    this,
                    "Please grant permission to access external storage.",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            // for Android 10 or lower, request the permission
            val permissions: Array<String>
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            } else {
                permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ||
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                showRationaleDialog(
                    "Drawing App",
                    "Drawing App needs to access your external storage"
                )
            } else {
                requestPermission.launch(permissions)
            }


        }
    }
    private fun showRationaleDialog(
        title: String,
        message: String,
    ){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){dialog, _->
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun requestCameraPermission() {
        when {
            // Check if permission is already granted
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted, proceed with camera operation
               val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }
            // Show rationale if permission was previously denied by user
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                AlertDialog.Builder(this)
                    .setTitle("Camera Permission")
                    .setMessage("This app requires camera permission to take pictures.")
                    .setPositiveButton("OK") { _, _ ->
                        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
            else -> {
                // Request permission if it was never requested before
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    //method to receive the photo taken from the camera intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val thumbNail : Bitmap = data!!.extras?.get("data") as Bitmap
            binding?.ivImage?.setImageBitmap(thumbNail)
            // Do something with the imageBitmap, such as displaying it in an ImageView
        }
    }
}