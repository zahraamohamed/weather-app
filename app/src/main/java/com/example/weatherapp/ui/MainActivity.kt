package com.example.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.weatherapp.Constant
import com.example.weatherapp.data.WeaterResponce
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private  lateinit var binding:ActivityMainBinding
    private val client=OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.btn.setOnClickListener {
            makeRequest(binding.edt.text.toString())
        }
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
                       binding.view.text= result.sys.countryId


                   }
               }
            }

        })
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
    /*
     *methods
     */


    fun <T> T.lazyLog(tag: String = "LAZY_LOG"): T {
        Log.i(tag, toString())
        return this
    }
}