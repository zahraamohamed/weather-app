package com.example.weatherapp.data

import com.google.gson.annotations.SerializedName

data class Weather(
    val id:Int,
    @SerializedName("main") val weatherState:String,
    val description:String,
    val icon:String,
)
