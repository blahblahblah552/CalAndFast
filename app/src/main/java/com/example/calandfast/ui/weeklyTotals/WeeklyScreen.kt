package com.example.calandfast.ui.weeklyTotals

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calandfast.InventoryTopAppBar
import com.example.calandfast.R
import com.example.calandfast.ui.AppViewModelProvider
import com.example.calandfast.ui.navigation.NavigationDestination
import com.example.calandfast.ui.theme.CalAndFastTheme
import java.time.DayOfWeek

object WeeklyCaloriesDestination : NavigationDestination {
    override val route = "weekly_calories"
    override val titleRes = R.string.week_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    weeklyViewModel: WeeklyViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = weeklyViewModel.uiState.collectAsState()
    val currentWeekCal = weeklyViewModel.initThisWeek(uiState.value.itemList)
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(WeeklyCaloriesDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },

        modifier = modifier,
    ) { innerPadding ->
        val weekList = weeklyViewModel.mapToList(currentWeekCal)
            Column {
            Row {

                BarChart(
                    modifier,
                    weekList
                )
            }
            Row {
                WeeklyCalorieBody(
                    currentWeekCal,
                    weekList.sum().toInt(),
                    modifier = Modifier
                        .padding(
                            start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                            end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                        )
                        .verticalScroll(rememberScrollState())
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun WeeklyCalorieBody(
    currentWeekCal: Map<DayOfWeek, Int>,
    totalWeeklyCalories: Int, // Pass as parameter
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ){
        Row {
            currentWeekCal.forEach { (dayOfWeek, calories) ->
                Text(
                    text = "${dayOfWeek.name.take(3)}\n$calories",
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Text(
            text = "Total Calories: $totalWeeklyCalories",
            textAlign = TextAlign.Center
        )
    }
}
private val defaultMaxHeight = 500.dp

@Composable
internal fun BarChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    maxHeight: Dp = defaultMaxHeight
) {
    val borderColor = MaterialTheme.colorScheme.primary
    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }

    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(maxHeight)
                .drawBehind {
                    // draw X-Axis
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                    // draw Y-Axis
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth
                    )
                }
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {

        values.forEach { item ->
            Bar(
                value = item/100,
                color = getDynamicColor(item/2000),
                maxHeight = values.max().dp
            )
        }
    }
}
fun getDynamicColor(progress: Float): Color {
    if (progress <= 0f) return Color.Blue
    if (progress >= 1f) return Color.Red

    val hueStart = 240.0f // Blue
    val hueEnd = 0.0f     // Red
    val hue = hueStart + (hueEnd - hueStart) * progress

    return Color.hsv(hue, 1f, 1f)
}

@Composable
private fun RowScope.Bar(
    value: Float,
    color: Color,
    maxHeight: Dp
) {
    val itemHeight = remember(value) { value * maxHeight.value / 100 }
    Spacer(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .height(itemHeight.dp)
            .weight(1f)
            .background(color)
    )
}

////////////// previews



@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    CalAndFastTheme{
        WeeklyCalorieBody(currentWeekCal = mapOf(DayOfWeek.MONDAY to 100, DayOfWeek.TUESDAY to 200,
            DayOfWeek.WEDNESDAY to 300, DayOfWeek.THURSDAY to 400, DayOfWeek.FRIDAY to 500,
            DayOfWeek.SATURDAY to 600, DayOfWeek.SUNDAY to 700),
            2000,
            modifier = Modifier)
    }
}
@Preview(showBackground = true)
@Composable
fun BarChartPreview() {
    CalAndFastTheme{
        BarChart(modifier = Modifier, values = listOf(100f,200f,300f,400f,500f,600f,700f))
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyScreenPreview() {
    CalAndFastTheme{
        WeeklyScreen(modifier = Modifier, navigateBack = {})
    }
}
