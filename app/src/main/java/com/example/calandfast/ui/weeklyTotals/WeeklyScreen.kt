package com.example.calandfast.ui.weeklyTotals

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calandfast.InventoryTopAppBar
import com.example.calandfast.R
import com.example.calandfast.ui.AppViewModelProvider
import com.example.calandfast.ui.navigation.NavigationDestination
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
    viewModel: WeeklyViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val currentWeekCal = viewModel.initThisWeek(uiState.value.itemList)
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
        Column {
            Row {
                WeeklyCalorieBody(
                    currentWeekCal,
                    modifier = Modifier
                        .padding(
                            start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                            top = innerPadding.calculateTopPadding(),
                            end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                        )
                        .verticalScroll(rememberScrollState())
                )
            }
            Row {
                val weekList = viewModel.mapToList(currentWeekCal)
                BarChart(
                    modifier,
                    weekList
                )
            }
        }
    }
}

@Composable
private fun WeeklyCalorieBody(
    currentWeekCal: Map<DayOfWeek,Int>,
    modifier: Modifier = Modifier
){

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Text(
            text= "Monday ${currentWeekCal.getOrDefault(DayOfWeek.MONDAY,0)}"
        )
        Text(
            text= "Tuesday ${currentWeekCal.getOrDefault(DayOfWeek.TUESDAY,0)}"
        )
        Text(
            text= "Wednesday ${currentWeekCal.getOrDefault(DayOfWeek.WEDNESDAY,0)}"
        )
        Text(
            text= "Thursday ${currentWeekCal.getOrDefault(DayOfWeek.THURSDAY,0)}"
        )
        Text(
            text= "Friday ${currentWeekCal.getOrDefault(DayOfWeek.FRIDAY,0)}"
        )
        Text(
            text= "Saturday ${currentWeekCal.getOrDefault(DayOfWeek.SATURDAY,0)}"
        )
        Text(
            text= "Sunday ${currentWeekCal.getOrDefault(DayOfWeek.SUNDAY,0)}"
        )
    }
}
private val defaultMaxHeight = 200.dp

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
                color = MaterialTheme.colorScheme.primary,
                maxHeight = values.max().dp
            )
        }
    }
}

@Composable
private fun RowScope.Bar(
    value: Float,
    color: androidx.compose.ui.graphics.Color,
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