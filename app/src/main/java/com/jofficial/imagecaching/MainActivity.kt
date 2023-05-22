package com.jofficial.imagecaching

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.jofficial.imagecaching.api.ApiInterface
import com.jofficial.imagecaching.api.RetrofitClient
import com.jofficial.imagecaching.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var apiInterface: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)

        binding.btnGetimage.setOnClickListener {
            if (isNetworkAvailable()) {
                getImageFromServer()
            } else {
                displayNoInternet()
            }
        }



    }

    private fun getImageFromServer() {

        if (isNetworkAvailable()) {

            val call = apiInterface.getImage()

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        val imageUrl = response.raw().request.url.toString()
                        Picasso.get().load(imageUrl).into(binding.imageview)

                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // Handle failure here
                }
            })

        } else {
            displayNoInternet()
        }
    }



    fun isNetworkAvailable(): Boolean {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable && activeNetworkInfo.isConnected
    }


    fun displayNoInternet() {
        Toast.makeText(this, R.string.no_network_connection, Toast.LENGTH_SHORT).show()
    }


    override fun onResume() {
        if (!isNetworkAvailable()){
            displayNoInternet()
        }
        super.onResume()
    }

}