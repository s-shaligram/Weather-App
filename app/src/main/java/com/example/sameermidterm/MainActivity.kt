package com.example.sameermidterm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.sameermidterm.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private lateinit var query: TextView
    private lateinit var weatherViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        query.text = binding.editTextCity.toString()

//        weatherViewModel.searchWeather(view).observe(this , Observer<> {  })

    }

    fun searchWeather(view: View){
        getWeather().start()
    }

    private fun getWeather(): Thread{

        return Thread {
            val url = URL("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/London%2C%20Ontario?unitGroup=metric&elements=datetime%2Ctempmax%2Ctempmin%2Ctemp%2Cprecipprob%2Cdescription&include=current&key=BNR9A4ND6HNMB3WX2RTE7R63Q&contentType=json")
            val connection  = url.openConnection() as HttpsURLConnection
            if(connection.responseCode == 200)
            {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val request = Gson().fromJson(inputStreamReader, Weather::class.java)
                updateUI(request)
                inputStreamReader.close()
                inputSystem.close()
            }
            else
            {
                binding.tvDescrip.text = "Failed Connection"
            }
        }
    }

    private fun updateUI(request: Weather){
        runOnUiThread{
            kotlin.run {
                binding.tvLocation.text = "Location: "+request.address+" on "+request.days?.get(0)?.datetime
                binding.tvCurrent.text = "Current Temperature: "+request.currentConditions.temp+" at "+request.currentConditions.datetime
                binding.tvMax.text = String.format("Maximum Temperature: %.1f",request.days?.get(0)?.tempmax)
                binding.tvMin.text = String.format("Minimum Temperature: %.1f",request.days?.get(0)?.tempmin)
                binding.tvPrecip.text = String.format("POP: %.1f",request.days?.get(0)?.precipprob)
                binding.tvDescrip.text = "Description: " + request.days?.get(0)?.description
//                binding.tvCurrent.text = String.format("Current Temperature: %.1f",request.currentConditions.temp)
//                binding.tvMax.text = String.format("Maximum Temperature: %.1f",request.days?.get(0)?.tempmax)
//                binding.tvMin.text = String.format("Minimum Temperature: %.1f",request.days?.get(0)?.tempmin)
//                binding.tvPrecip.text = String.format("POP: %.1f",request.days?.get(0)?.precipprob)
//                binding.tvDescrip.text = "Description: " + request.days?.get(0)?.description
            }
        }
    }
}