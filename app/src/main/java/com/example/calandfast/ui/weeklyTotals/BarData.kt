package com.example.calandfast.ui.weeklyTotals

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek


// Data class for bar data
data class BarData(val value: Float, val color: Color)

@Composable
private fun WeeklyCalorieBody(
    currentWeekCal: Map<DayOfWeek, Int>,
    totalWeeklyCalories: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        currentWeekCal.forEach { (dayOfWeek, calories) ->
            Text(
                text = "${dayOfWeek.name.take(3)}\n$calories",
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.Center
            )
        }
        Text(
            text = "Total Calories: $totalWeeklyCalories",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BarChart(
    barDataList: List<BarData>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        barDataList.forEach { barData ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Canvas(
                    modifier = Modifier
                        .size(width = 20.dp, height = (barData.value * 5).coerceIn(0f, 100f).dp) //Scale height
                        .background(barData.color)
                ) {
                    // Drawing happens here!  No explicit 'onDraw' is needed for simple shapes.
                }
                Text(
                    text = barData.value.toInt().toString()
                )
            }
        }
    }
}

///Previews

