//package com.example.weatherapp.adapter
//
//import android.view.LayoutInflater
//import com.example.weatherapp.databinding.RawItemBinding
//
//import android.view.ViewGroup
//import android.view.animation.AnimationUtils
//import androidx.recyclerview.widget.RecyclerView
//import com.example.weatherapp.data.WeaterResponce
//import com.google.gson.Gson
//import okhttp3.Call
//import okhttp3.Callback
//import okhttp3.Request
//import okhttp3.Response
//import java.io.IOException
//import java.text.SimpleDateFormat
//import java.util.*
//
//
//class adapter() : RecyclerView.Adapter<adapter.ViewHolder>() {
//
//    //link this fragment with layout source
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
//        val view: RawItemBinding = RawItemBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return ViewHolder(view)
//    }
//    //show all components in cardView
//    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
//
//       holder.binding.apply {
//           //build url by okhttp library
//           val request= Request.Builder().url(getUrl(country)).build()
//
//           //convert json file to string(parser json file) by using Gson library
//           client.newCall(request).enqueue(object : Callback {
//
//
//               override fun onFailure(call: Call, e: IOException) {
//                   e.message?.let { lazyLog(it) }
//               }
//
//               override fun onResponse(call: Call, response: Response) {
//                   response.body?.string()?.let { jsonString->
//
//                       val result= Gson().fromJson(jsonString, WeaterResponce::class.java)
//
//
//                       runOnUiThread{
//                           binding.apply {
//                               binding.country.text= result.country
//
//                               //set date
//                               (SimpleDateFormat(
//                                   "dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(result.dt.toLong()*1000))).also { binding.date.text = it }
//
//
//
//                               val temp =(result.main.temp/10)
//                               tempTxt.text= String.format("%.1f", temp)+"°C"
//
//                               windTxt.text=result.wind.speed.toString()
//                               hueTxt.text=result.main.humidity.toString()+"%"
//                               setDataToChart(result.main.temp_max,result.main.temp_min)
//
//                               setWeatherIcon(result.weather[0].id)
//
//    }
//    //to get size of list
//
//
//
//}
//
//    override fun getItemCount()=100
//
//    //to get all views in row item and with binding to make easy process
//    class ViewHolder(val binding: RawItemBinding?) : RecyclerView.ViewHolder(binding?.root!!)
//}
