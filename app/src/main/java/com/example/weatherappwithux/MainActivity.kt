package com.example.weatherappwithux

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest

import com.android.volley.toolbox.Volley

import com.example.weatherappwithux.MainScreen.MainCard
import com.example.weatherappwithux.MainScreen.TabLoyaout
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dayslist = remember {
                mutableStateOf(listOf<WeatherModel>())
            }
            val currentday = remember {
                mutableStateOf(WeatherModel(
                    "",
                    "" ,
                    "0.0" ,
                    "",
                    " ",
                    "0.0",
                    "0.0",
                    ""
                ))
            }
            getdata("Almaty" , this , dayslist , currentday)
            val dialogstate = remember{
                mutableStateOf(false)
            }

            if(dialogstate.value){
                DialogSearch(dialogstate , Submit = {
                    getdata(it , this , dayslist , currentday)
                })
            }

            Image(painter = painterResource(R.drawable.i)  ,
                contentDescription = "weather" ,
                modifier = Modifier.fillMaxSize().alpha(0.8f),
                contentScale = ContentScale.FillBounds
            )
            Column {
                MainCard(
                    currentday,
                    search ={
                        getdata("Almaty" , this@MainActivity , dayslist , currentday)
                    } , dialogstate
                )
                TabLoyaout(dayslist , currentday)
            }


        }
    }
}

const val  API  = "34da9c2cf51f434386f63928252401"

fun getdata(city:String , context: Context  , daysList:MutableState<List<WeatherModel>> , currentday :MutableState<WeatherModel>){
    val url = "https://api.weatherapi.com/v1/forecast.json?key=$API&q=$city&days=3&aqi=no&alerts=no"
    val queue = Volley.newRequestQueue(context)
    val sRequest = StringRequest(
        Request.Method.GET,
        url ,
        {
                response->
            val list = getweatherdays(response)
            currentday.value = list[0]
            daysList.value =list


        },
        {
                error-> Log.e("MyLog" , "error$error")
        }

    )

    queue.add(sRequest)



}


private fun getweatherdays(response:String):List<WeatherModel>{
    if(response.isEmpty()){
        return listOf()
    }
    val list =  ArrayList<WeatherModel>()
    val mainObject = JSONObject(response)
    val days = mainObject.getJSONObject("forecast" ).getJSONArray("forecastday")
    val city = mainObject.getJSONObject("location").getString("name")

    for(i in 0 until days.length()){
        val item = days[i] as JSONObject
        list.add(
            WeatherModel(
                city,
                item.getString("date") ,
                "" ,
                item.getJSONObject("day").getJSONObject("condition").getString("text"),
                item.getJSONObject("day").getJSONObject("condition").getString("icon"),
                item.getJSONObject("day").getString("maxtemp_c"),
                item.getJSONObject("day").getString("mintemp_c"),
                item.getJSONArray("hour").toString()
            )
        )

    }


    list[0] = list[0].copy(
        time = mainObject.getJSONObject("current").getString("last_updated") ,
        tempCurrent = mainObject.getJSONObject("current").getString("temp_c")
    )

    return list

}

