package com.example.unilocal.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.unilocal.Adapter
import com.example.unilocal.ImagePicker
import com.example.unilocal.R
import com.example.unilocal.adapter.ImagePagerAdapter
import com.example.unilocal.databinding.ActivityRegisterPlaceBinding
import com.example.unilocal.db.Schedules
import com.example.unilocal.fragment.TimePickerFragment
import com.example.unilocal.model.Place
import com.example.unilocal.model.PlaceStatus
import com.example.unilocal.model.Schedule
import com.example.unilocal.model.User
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
//import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class RegisterPlaceActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityRegisterPlaceBinding
    lateinit var viewPager: ViewPager
    lateinit var layouts:IntArray
    lateinit var adapter: Adapter
    lateinit var user:User
    var init:Boolean = true
    lateinit var btn_next: Button
    var selected_categorie:Int = 0
    var categorie_to_register:Int = 0
    lateinit var categorie_string: String
    val dots = mutableListOf<TextView>()
    lateinit var position_layout:LinearLayout
    var condition = false
    var codigoArchivo = 0
    lateinit var dialog: Dialog


    //FORM 1 VARS
    lateinit var input_place_name:EditText
    lateinit var input_place_description:EditText
    lateinit var input_open_hour:EditText
    lateinit var input_close_hour:EditText
    lateinit var input_place_phone:EditText
    lateinit var input_place_secundary_phone:EditText
    lateinit var input_place_adress:EditText


    lateinit var place_name:String
    lateinit var place_description:String
    lateinit var open_hour:String
    lateinit var close_hour:String
    lateinit var place_phone:String
    lateinit var place_secundary_phone:String
    lateinit var place_adress:String

    //FORM 3 VARS
    lateinit var btn_hotel_categorie:Button
    lateinit var btn_coffe_categorie:Button
    lateinit var btn_restaurant_categorie:Button
    lateinit var btn_park_categorie:Button
    lateinit var btn_bar_categorie:Button

    //FORM 4 VARS
    var images:ArrayList<String> = ArrayList()//mutableListOf<Uri>()
    //private lateinit var viewPager2: ViewPager
    private lateinit var images_sel: LinearLayout
    private lateinit var images_sel2: LinearLayout
    private lateinit var imagePicker: ImagePicker
    private lateinit var resultLauncher:ActivityResultLauncher<Intent>

    // MAPS

    private lateinit var google_map: GoogleMap
    private var tienePermiso = false
    private val defaultLocation = LatLng(4.550923, -75.6557201)
    private lateinit var permissionsResultCallback:ActivityResultLauncher<String>
    private var lat: Double? = null
    private var lng: Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.progress_dialog_layout)
        dialog = builder.create()

        viewPager = binding.formPager

        viewPager.offscreenPageLimit = 3
        layouts = intArrayOf(
            R.layout.activity_register_place_form_1,
            R.layout.activity_register_place_form_2,
            R.layout.activity_register_place_form_3,
            R.layout.activity_register_place_form_4
        )

        adapter = Adapter(this, layouts)
        viewPager.adapter = adapter


        btn_next = binding.Next

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult() ) {
            onActivityResult(it.resultCode, it)

        }


        imagePicker = ImagePicker(this)

        binding.takePhoto.visibility = View.GONE

        btn_next.setOnClickListener{
            if( btn_next.text == getString(R.string.register_user_finish)){
                register()
            }else if (btn_next.text == getString(R.string.register_place_choose_photos)){
                selectMedia()
                //imagePicker.pickImages()
            }else{
                nextListener()
            }
        }

        binding.takePhoto.setOnClickListener{
            takePhoto()
        }



        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {


            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if(init){
                    if(position == 0){
                        initVars()
                        initOnclickListeners()
                        init = false
                    }
                }
            }

            override fun onPageSelected(position: Int) {

                if(position == 1){
                    verifyForm1Inputs()
                }
                if(position==3) {
                    if(images.isEmpty()){
                        btn_next.text = getString(R.string.register_place_choose_photos)
                        binding.takePhoto.visibility = View.VISIBLE
                    }else{
                        btn_next.text = getString(R.string.register_user_finish)
                        binding.takePhoto.visibility = View.GONE
                    }
                }else{
                    btn_next.text = getString(R.string.register_user_next)
                    binding.takePhoto.visibility = View.GONE
                }

            }
            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        permissionsResultCallback = registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
            when (it) {
                true -> { println("Permiso aceptado") }
                false -> { println("Permiso denegado") }
            }
        }

        getLocationPermission()
    }

    private fun selectMedia() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        codigoArchivo = 2
        resultLauncher.launch(i)
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                resultLauncher.launch(takePictureIntent)
                codigoArchivo = 1
            }
        }
    }

    private fun register() {
        if(place_name.isNotEmpty() && place_description.isNotEmpty() && open_hour.isNotEmpty() && close_hour.isNotEmpty() && place_phone.isNotEmpty()
            && place_secundary_phone.isNotEmpty() && place_adress.isNotEmpty() && categorie_string.isNotEmpty() && images.isNotEmpty() && verifyRegexPhone() && lat != null && lng != null){

            if(verifyHours(open_hour, close_hour) && verifyPhones()){
                setDialog(true)
                val user = FirebaseAuth.getInstance()

                if(user != null){

                val placeRegister = Place( place_name, place_description, user.uid!!, PlaceStatus.PENDING, categorie_to_register, place_adress,
                    lat!!, lng!!, 1, 1, 1)
                placeRegister.phoneNumbers.add(place_phone)
                placeRegister.phoneNumbers.add(place_secundary_phone)
                placeRegister.images = images
                val only_open_hour = determinHour(open_hour)
                val only_close_hour = determinHour(close_hour)
                val schedule1 = Schedule(5, Schedules.getAll(),only_open_hour,only_close_hour)
                placeRegister.schedules.add(schedule1)

                //Places.add(placeRegister)
                Firebase.firestore
                    .collection("places")
                    .add(placeRegister)
                    .addOnSuccessListener {
                        //Snackbar.make(binding.root, getString(R.string.register_place_alerts_registered), Snackbar.LENGTH_LONG).show()
                        Toast.makeText(this, getString(R.string.register_place_alerts_registered), Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({finish()}, 4000)
                        goToMap()
                    }
                    .addOnFailureListener { e ->
                        Snackbar.make(binding.root, "$e.message", Snackbar.LENGTH_LONG).show()
                    }
                }

                setDialog(false)

            }else{
                Toast.makeText(this, getString(R.string.register_place_alerts_fields_filled), Toast.LENGTH_SHORT).show()
            }
        }else{
            verifyForm1Inputs()
            Toast.makeText(this, getString(R.string.register_user_msg_all_inpts_obligatories), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }


    private fun goToMap() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }

    private fun determinHour(hour:String): Int {
        val parts = hour.split(":")
        val open_hours = parts[0].toInt()
        return open_hours
    }

    private fun onActivityResult(resultCode:Int, result: ActivityResult){
        if(images.size == 6){
            btn_next.text = getString(R.string.register_user_finish)
            binding.takePhoto.visibility = View.GONE
        }else {
            if (resultCode == Activity.RESULT_OK) {
                setDialog(true)
                val fecha = Date()
                val storageRef = FirebaseStorage.getInstance()
                    .reference
                    .child("/p-${fecha.time}.jpg")
                if (codigoArchivo == 1) {
                    val data = result.data?.extras
                    if (data?.get("data") is Bitmap) {
                        val imageBitmap = data?.get("data") as Bitmap
                        val baos = ByteArrayOutputStream()
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()
                        storageRef.putBytes(data).addOnSuccessListener {
                            storageRef.downloadUrl.addOnSuccessListener {
                                dibujarImagen(it)
                            }
                        }.addOnFailureListener {
                            Snackbar.make(binding.root, "${it.message}", Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }

                    //Imagen capturada desde la cámara del celular
                } else if (codigoArchivo == 2) {
                    val data = result.data
                    if (data != null) {
                        val selectedImageUri: Uri? = data.data
                        if (selectedImageUri != null) {
                            storageRef.putFile(selectedImageUri).addOnSuccessListener {
                                storageRef.downloadUrl.addOnSuccessListener {
                                    dibujarImagen(it)
                                }
                            }.addOnFailureListener {
                                Snackbar.make(binding.root, "${it.message}", Snackbar.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }

                    //Archivo seleccionado desde el almacenamiento
                }
            }
        }

    }

    private fun dibujarImagen(url: Uri?) {
        images.add(url.toString())

        var imagen = ImageView(applicationContext)
        imagen.layoutParams = LinearLayout.LayoutParams(300, 310)
        if(images.size <= 3){
            Log.e("RegisterPlace", "Es menor a 3: ${images.size}")
            images_sel.addView(imagen)
        }else{
            Log.e("RegisterPlace", "Es mayor a 3: ${images.size}")
            images_sel2.addView(imagen)
        }

        Glide.with( applicationContext )
            .load(url.toString())
            .into(imagen)

        if(images.size == 6){
            btn_next.text = getString(R.string.register_user_finish)
            binding.takePhoto.visibility = View.GONE
        }
        setDialog(false)
    }


    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ImagePicker.PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK) {
            images = mutableListOf()

            data?.let {
                val clipData = it.clipData
                if (clipData != null) {
                    for (i in 0 until clipData.itemCount) {
                        images.add(clipData.getItemAt(i).uri)
                    }
                } else {
                    it.data?.let { uri ->
                        images.add(Uri.parse(uri.toString()))
                    }
                }
            }
            val adapter = ImagePagerAdapter(this, images)
            viewPager2.adapter = adapter
            btn_next.text = getString(R.string.register_user_finish)
            initDots()
        }
    }*/


    private fun verifyForm1Inputs(){
        getInputsText()
        verifyRegexPhone()
        /*verifyRegexEmail()
        verifyRegexPass()
        verifyDatesWithDb()*/
        if(place_phone.equals(place_secundary_phone)){
            input_place_secundary_phone.error = getString(R.string.register_place_alerts_same_numbers)
        }
        if(open_hour.isEmpty()){
            input_open_hour.error = getString(R.string.forgot_msg_obligatorie_inputs)
        }else{
            input_open_hour.error = null
        }
        if(close_hour.isEmpty()){
            input_close_hour.error = getString(R.string.forgot_msg_obligatorie_inputs)
        }else{
            input_close_hour.error = null
        }
        if(place_name.isEmpty()){
            input_place_name.error = getString(R.string.forgot_msg_obligatorie_inputs)
        }
        if(place_description.isEmpty()){
            input_place_description.error = getString(R.string.forgot_msg_obligatorie_inputs)
        }
        if(place_phone.isEmpty()){
            input_place_phone.error = getString(R.string.forgot_msg_obligatorie_inputs)
        }
        if(place_secundary_phone.isEmpty()){
            input_place_secundary_phone.error = getString(R.string.forgot_msg_obligatorie_inputs)
        }
        if(place_adress.isEmpty()){
            input_place_adress.error = getString(R.string.forgot_msg_obligatorie_inputs)
        }
    }

    private fun getInputsText() {
        //FORM 1 INPUTS
        place_name = input_place_name.text.toString()
        place_description = input_place_description.text.toString()
        open_hour = input_open_hour.text.toString()
        close_hour = input_close_hour.text.toString()
        place_phone = input_place_phone.text.toString()
        place_secundary_phone = input_place_secundary_phone.text.toString()
        place_adress = input_place_adress.text.toString()

        verifyHours(open_hour, close_hour)
    }

    private fun initOnclickListeners(){
        input_open_hour.setOnClickListener{
            showTimePickerDialog(input_open_hour)
        }
        input_close_hour.setOnClickListener{
            showTimePickerDialog(input_close_hour)
        }
        btn_hotel_categorie.setOnClickListener{
            changeButton(btn_hotel_categorie)
            categorie_string = getString(R.string.register_place_categorie_hotel)
            categorie_to_register = 1
        }
        btn_coffe_categorie.setOnClickListener{
            changeButton(btn_coffe_categorie)
            categorie_string = getString(R.string.register_place_categorie_cafe)
            categorie_to_register = 2
        }
        btn_restaurant_categorie.setOnClickListener{
            changeButton(btn_restaurant_categorie)
            categorie_string = getString(R.string.register_place_categorie_restaurant)
            categorie_to_register = 3
        }
        btn_park_categorie.setOnClickListener{
            changeButton(btn_park_categorie)
            categorie_string = getString(R.string.register_place_categorie_park)
            categorie_to_register = 4
        }
        btn_bar_categorie.setOnClickListener{
            changeButton(btn_bar_categorie)
            categorie_string = getString(R.string.register_place_categorie_bar)
            categorie_to_register = 5
        }

    }

    private fun initVars(){

        //FORM 1 VARS
        input_place_name = viewPager.findViewById(R.id.place_name)
        input_place_description = viewPager.findViewById(R.id.place_description)
        input_open_hour = viewPager.findViewById(R.id.place_open_hour)
        input_close_hour = viewPager.findViewById(R.id.place_close_hour)
        input_place_phone = viewPager.findViewById(R.id.place_phone)
        input_place_secundary_phone = viewPager.findViewById(R.id.place_secundary_phone)
        input_place_adress = viewPager.findViewById(R.id.place_adress)

        //FORM 2 VARS

        val mapFragment = supportFragmentManager.findFragmentById( R.id.mapa_lugar) as SupportMapFragment
        mapFragment.getMapAsync(this)


        //FORM 3 VARS

        btn_hotel_categorie = viewPager.findViewById(R.id.btn_hotel)
        btn_coffe_categorie = viewPager.findViewById(R.id.btn_coffe)
        btn_restaurant_categorie = viewPager.findViewById(R.id.btn_restaurant)
        btn_park_categorie = viewPager.findViewById(R.id.btn_park)
        btn_bar_categorie = viewPager.findViewById(R.id.btn_bar)

        //FORM 4 VARS

        images_sel = viewPager.findViewById(R.id.images_sel)
        images_sel2 = viewPager.findViewById(R.id.images_sel2)
        //position_layout = viewPager.findViewById(R.id.position_layout_place)
        //initDots()
    }


    fun changeButton (btn:Button){
        if (btn.isSelected) {
            btn.setTextColor(resources.getColor(R.color.black))
            btn.isSelected = false
            selected_categorie--
        } else {
            if(selected_categorie == 1){
                Toast.makeText(this,getString(R.string.register_user_msg_invalid_category),Toast.LENGTH_LONG).show()
            }else{
                btn.setTextColor(resources.getColor(R.color.white))
                btn.isSelected = true
                selected_categorie++
            }
        }
    }

    fun nextListener (){
        if (viewPager.currentItem+1 < layouts.size){
            viewPager.setCurrentItem(viewPager.currentItem+1)
        }
    }

    private fun showTimePickerDialog(view: EditText) {
        val timePicker = TimePickerFragment {onTimeSelected(it, view)}
        timePicker.show(supportFragmentManager, "time")
    }

    private fun onTimeSelected (time:String, view:EditText){{}
        view.setText("$time")
        verifyForm1Inputs()
    }

    private fun verifyRegexPhone(): Boolean {
        if(place_phone.length == 10 && place_secundary_phone.length == 10){
            return true
        }else{
            if(place_phone.length != 10){
                input_place_phone.error = getString(R.string.register_user_msg_invalid_phone)
            }
            if(place_secundary_phone.length != 10){
                input_place_secundary_phone.error = getString(R.string.register_user_msg_invalid_phone)
            }
            return false
        }
    }

    private fun verifyPhones(): Boolean {
        if(place_phone.equals(place_secundary_phone)){
            input_place_secundary_phone.error = getString(R.string.register_place_alerts_same_numbers)
            return false
        }else{
            return true
        }
    }

    private fun verifyHours(hour_open:String, hour_close:String): Boolean {
        if(hour_open.isNotEmpty() && hour_close.isNotEmpty()){
            val only_open_hour = determinHour(hour_open)
            val only_close_hour = determinHour(hour_close)
            if(only_open_hour > only_close_hour){
                Toast.makeText(this,getString(R.string.register_place_alerts_opening_larger),Toast.LENGTH_LONG).show()
                return false
            }
            return true
        }
        return false
    }

    /*private fun initDots() {
            for (i in 0 until (viewPager2.adapter?.count ?: 0)) {1
                val dot = TextView(this)
                dot.text = "•"
                dot.textSize = 30f
                dot.setTextColor(ContextCompat.getColor( this, R.color.inactive))
                dot.setOnClickListener { viewPager2.currentItem = i }
                dots.add(dot)
                position_layout.addView(dot)
                condition = true
            }
        if(dots.isNotEmpty()){
            dots.first().setTextColor(ContextCompat.getColor(this, R.color.active))
        }
    }*/

    override fun onMapReady(googleMap: GoogleMap) {
        google_map = googleMap
        google_map.uiSettings.isZoomControlsEnabled = true
        google_map.setOnMapClickListener {
            if(lat == null || lng == null){

            }
            lat = it.latitude
            lng = it.longitude
            google_map.clear()
            google_map.addMarker(MarkerOptions().position(it).title(getString(R.string.right_here)))

        }

        try{
            if(tienePermiso){
                google_map.isMyLocationEnabled = true
                google_map.uiSettings.isMyLocationButtonEnabled = true
            } else {
                google_map.isMyLocationEnabled = false
                google_map.uiSettings.isMyLocationButtonEnabled = false
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        getLocation()
    }

    private fun getLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            tienePermiso = true
        } else {
            permissionsResultCallback.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getLocation() {
        try {
            if (tienePermiso) {
                val ubicacionActual =
                    LocationServices.getFusedLocationProviderClient(this).lastLocation
                ubicacionActual.addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val ubicacion = it.result
                        if (ubicacion != null) {
                            val latlng = LatLng(ubicacion.latitude, ubicacion.longitude)
                            google_map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15F))
                            google_map.addMarker(MarkerOptions().position(latlng).title(getString(R.string.right_here)))
                        }
                    } else {
                        google_map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,
                            15F))
                        google_map.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

}