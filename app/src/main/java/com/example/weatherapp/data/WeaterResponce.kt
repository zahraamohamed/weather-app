package com.example.weatherapp.data

import com.google.gson.annotations.SerializedName

data class WeaterResponce(
    val coordinates:Coordinate,
    val weather:List<Weather>,
    val main:Main,
    val wind:Wind,
     val sys:Sys,
    @SerializedName("name")  val country:String,
)
