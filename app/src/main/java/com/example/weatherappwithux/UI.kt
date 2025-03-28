package com.example.weatherappwithux

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherappwithux.ui.theme.bluelight


@Composable
fun ListItem(item: WeatherModel , currentday: MutableState<WeatherModel>) {
    Card(modifier = Modifier
        .clickable {
            currentday.value = item
        }
        .fillMaxWidth()
        .padding(3.dp) ,
        backgroundColor = bluelight , elevation = 0.dp ,
        shape = RoundedCornerShape(5.dp),){
        Row(horizontalArrangement = Arrangement.SpaceBetween
            , modifier = Modifier.fillMaxWidth() ,
            verticalAlignment = Alignment.CenterVertically){
            Column (modifier = Modifier.padding(start = 5.dp , top = 5.dp , bottom = 5.dp)){
                Text(text = item.time)
                Text(text = item.condition, color = Color.White)
            }

            Text(text = item.tempCurrent.ifEmpty { "${item.maxtemp} / ${item.mintemp}"} , color = Color.White , fontSize = 25.sp)


            AsyncImage(

                model = "https:${item.icon}",
                contentDescription = "image5",
                modifier = Modifier
                    .size(35.dp)
            )

        }

    }
}


@Composable
fun MainList(list: List<WeatherModel> , currentday:MutableState<WeatherModel>){
    LazyColumn {
        itemsIndexed(
            list
        )
        {
          index , item->
            ListItem(item , currentday)
        }
    }
}



@Composable


fun DialogSearch(dialogstate: MutableState<Boolean> , Submit:(String)->Unit) {

    val dialogtext= remember{
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = {
        dialogstate.value = false

    }, confirmButton = {
        TextButton(onClick = {
            Submit(dialogtext.value)
            dialogstate.value = false

        }) {
            Text(text = "OK")
        }
    } ,
        dismissButton = {
            TextButton(onClick = {
                dialogstate.value = false

            }) {
                Text(text = "Cancel")
            }
        } ,



        title = {
            Column() {
                Text(text = "Put  into your country")
                TextField(value = dialogtext.value , onValueChange = {
                    dialogtext.value = it
                })

            }

        }


    )
}