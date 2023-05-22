package com.jofficial.imagecaching.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("/200/300")
    fun getImage(): Call<Void>

}