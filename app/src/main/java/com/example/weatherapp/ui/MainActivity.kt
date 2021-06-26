package com.example.weatherapp.ui

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.weatherapp.Constant
import com.example.weatherapp.R
import com.example.weatherapp.data.WeaterResponce
import com.example.weatherapp.databinding.ActivityMainBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Array.getLong
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private  lateinit var binding:ActivityMainBinding
    private val client=OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        searchCountry()
        showChartOfTemp()
        makeRequest("iraq")
//        makeRequest(binding.search.query.toString())
//        makeRequest("iraq")

//        binding.btn.setOnClickListener {
//
//
//        }

    }




    private fun makeRequest(country: String) {


        //build url by okhttp library
        val request=Request.Builder().url(getUrl(country)).build()

        //convert json file to string(parser json file) by using Gson library
        client.newCall(request).enqueue(object :Callback{


            override fun onFailure(call: Call, e: IOException) {
                e.message?.let { lazyLog(it) }
            }

            override fun onResponse(call: Call, response: Response) {
               response.body?.string()?.let { jsonString->

                   val result=Gson().fromJson(jsonString,WeaterResponce::class.java)


                   runOnUiThread{
                        binding.apply {
                       binding.country.text= result.country

                        //set date
                       (SimpleDateFormat(
                           "dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(result.dt.toLong()*1000))).also { binding.date.text = it }

                          weatherState.text=gradientColor(result.weather.joinToString { it.description })

                            val temp =(result.main.temp/10)
                           tempTxt.text= String.format("%.1f", temp)+"°C"

                           windTxt.text=result.wind.speed.toString()
                           hueTxt.text=result.main.humidity.toString()+"%"
                            setDataToChart(result.main.temp_max,result.main.temp_min)
                            //show icon image in api
//                          val icon=result.weather.joinToString { it.icon }
//                            val iconUrl="http://openweathermap.org/img/w/$icon.png"
//                            Glide.with(this@MainActivity).load(iconUrl).into(iconWeather)
                            setWeatherIcon(result.weather[0].id)
                            sunsetTxt.text= SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date((result.sys.sunset).toLong()*1000))
                            sunriseTxt.text= SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date((result.sys.sunrise).toLong()*1000))

                        }}
               }
            }

        })
    }

    /*
       *methods
       */
    fun setWeatherIcon(weatherCode: Int) {
        binding.apply {
            when {
                weatherCode / 100 == 2 -> {
                    Glide.with(this@MainActivity).load(R.drawable.ic_thunderstorm).into(iconWeather)
                }
                weatherCode / 100 == 3 -> {
                    Glide.with(this@MainActivity).load(R.drawable.ic_cloudy__1_).into(iconWeather)
                }
                weatherCode / 100 == 5 -> {
                    Glide.with(this@MainActivity).load(R.drawable.ic_cloudy__1_).into(iconWeather)
                }
                weatherCode / 100 == 6 -> {
                    Glide.with(this@MainActivity).load(R.drawable.ic_snowy).into(iconWeather)
                }
                weatherCode / 100 == 7 -> {
                    Glide.with(this@MainActivity).load(R.drawable.ic_cloudy__1_).into(iconWeather)
                }
                weatherCode == 800 -> {
                    Glide.with(this@MainActivity).load(R.drawable.sun_cloud).into(iconWeather)
                }
                weatherCode == 801 -> {
                    Glide.with(this@MainActivity).load(R.drawable.ic_cloudy__1_).into(iconWeather)
                }
                weatherCode == 803 -> {
                    Glide.with(this@MainActivity).load(R.drawable.ic_cloudy__1_).into(iconWeather)
                }
                weatherCode / 100 == 8 -> {
                    Glide.with(this@MainActivity).load(R.drawable.sun_cloud).into(iconWeather)
                }
            }
        }
    }
    private fun gradientColor(State:String): String {
        binding.apply {
        val paint = weatherState.paint
        val width = paint.measureText(State)
        val textShader: Shader = LinearGradient(0f, 0f, width, weatherState.textSize, intArrayOf(
            Color.parseColor("#ffffff"),
            Color.parseColor("#B3BEFF"),

            ), null, Shader.TileMode.REPEAT)
        weatherState.paint.shader = textShader

    }
        return State

    }

    private fun showChartOfTemp() {
        binding.apply {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = " max and min temperature"
        //hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
            pieChart.centerText="temperature"
            pieChart.setTransparentCircleColor(CONTEXT_IGNORE_SECURITY)
        //adding padding
        pieChart.setExtraOffsets(20f, 0f, 20f, 20f)
        pieChart.setUsePercentValues(true)
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.isWordWrapEnabled = true}
    }
    private fun  setDataToChart(max:Float,min:Float){
        binding.pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        dataEntries.add(PieEntry(max, "max temperature"))
        dataEntries.add(PieEntry(min, "min temperature"))


        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#FF8092FB"))
        colors.add(Color.parseColor("#B2BEFF"))


        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // In Percentage
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        binding.pieChart.data = data
        data.setValueTextSize(15f)
        binding.pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)

        //create hole in center
        binding.apply {
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)}
    }




    /*
    *this function return api url
     */
    private fun getUrl(country: String): HttpUrl {
        return HttpUrl.Builder()
            .scheme(Constant.SCHEMA)
            .host(Constant.HOST)
            .addPathSegment(Constant.PATH)
            .addQueryParameter(Constant.RATE_LIMITER_TYPE, country)
            .addQueryParameter(Constant.API_KEY_QUERY, Constant.API_KEY_VALUE)
            .build()
    }

    // search of country
    private fun  searchCountry(){
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                makeRequest(query!!.lowercase(Locale.getDefault()))
                if(query.isNullOrEmpty() )

                {
                    Toast.makeText(applicationContext, "country not found", Toast.LENGTH_LONG).show()

                }else {
                    makeRequest(query.lowercase(Locale.getDefault()))
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }

        })
    }



    fun <T> T.lazyLog(tag: String = "LAZY_LOG"): T {
        Log.i(tag, toString())
        return this
    }
}