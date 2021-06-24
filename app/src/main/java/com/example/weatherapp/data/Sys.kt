package com.example.weatherapp.data

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("country")   val countryId:String,
    @SerializedName("sunrise")   val sunrise:Int,
    @SerializedName("sunset")   val sunset:Int,
)
