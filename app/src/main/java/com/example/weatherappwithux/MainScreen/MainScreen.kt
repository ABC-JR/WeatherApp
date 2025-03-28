package com.example.weatherappwithux.MainScreen
import androidx.compose.material.Tab
import androidx.compose.material.TabRow

import com.google.accompanist.pager.pagerTabIndicatorOffset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherappwithux.MainList
import com.example.weatherappwithux.R
import com.example.weatherappwithux.WeatherModel
import com.example.weatherappwithux.ui.theme.bluelight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun MainCard(
    currentday: MutableState<WeatherModel>,
    search: () -> Unit,
    dialogstate: MutableState<Boolean>
) {




    Column(
        modifier = Modifier
            .padding(5.dp , top = 16.dp)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(bluelight)
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = currentday.value.time ,
                    color = Color.White,
                    fontSize = 15.sp
                )
                AsyncImage(
                    model = "https:${currentday.value.icon}",
                    contentDescription = "imagesun",
                    modifier = Modifier
                        .size(35.dp)
                        .padding(end = 8.dp)
                )

            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentday.value.city,
                    color = Color.White,
                    fontSize = 24.sp
                )
                Text(
                    text = if(currentday.value.tempCurrent.isNotEmpty())
                        currentday.value.tempCurrent.toFloat().toInt().toString() +"°С"
                    else "${currentday.value.maxtemp.toFloat().toInt()} °С / ${currentday.value.mintemp.toFloat().toInt()}",
                    color = Color.White,
                    fontSize = 36.sp
                )
                Text(
                    text = currentday.value.condition,
                    color = Color.White,
                    fontSize = 16.sp
                )


            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(onClick = {
                    dialogstate.value = true
                }) {

                    Icon(
                        painter = painterResource(R.drawable.baseline_search_24),
                        contentDescription = "im3", tint = Color.White
                    )


                }
                Text(
                    text = "${currentday.value.maxtemp.toFloat().toInt()} °С / ${currentday.value.mintemp.toFloat().toInt()}",
                    color = Color.White,
                    fontSize = 16.sp
                )
                IconButton(onClick = {

                    search.invoke()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_360_24),
                        contentDescription = "img4",
                        tint = Color.White
                    )


                }
            }


        }
    }

}

@OptIn(ExperimentalPagerApi::class)

@Composable
fun TabLoyaout(daysList: MutableState<List<WeatherModel>> , currentday: MutableState<WeatherModel>) {
    val tablist = listOf("HOURS", "DAYS")
    val pagerState = rememberPagerState()
    val tabindex = pagerState.currentPage
    val coroutScrot = rememberCoroutineScope()






    Column(
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(5.dp))
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                androidx.compose.material3.TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            },
            contentColor = Color.White, backgroundColor = bluelight ,
        ) {
            tablist.forEachIndexed { index, s ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutScrot.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(text = s) }
                )
            }
        }


        HorizontalPager(
            count = tablist.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()

        ) {
                index ->
            val list = when(index){
                0-> getweatherbyhours(currentday.value.hours)
                1-> daysList.value

                else -> {
                    daysList.value
                }
            }
            MainList(list, currentday)

        }

    }


}

private fun getweatherbyhours(hours:String) :List<WeatherModel>{
    if(hours.isEmpty()){
        return listOf()
    }


    val list =  ArrayList<WeatherModel>()
    val hoursArray = JSONArray(hours)

//    val city = hoursArray.getJSONObject("location").getString("name")

    for(i in 0 until hoursArray.length()){
        val item = hoursArray[i] as JSONObject
        list.add(
            WeatherModel(
                "",
                item.getString("time") ,
                item.getString("temp_c").toFloat().toInt().toString() + "°C" ,
                item.getJSONObject("condition").getString("text"),
                item.getJSONObject("condition").getString("icon"),
                "" , "" ,""
            )
        )

    }


    return list
}

