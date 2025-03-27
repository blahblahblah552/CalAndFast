package com.example.calandfast.ui.weeklyTotals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calandfast.InventoryTopAppBar
import com.example.calandfast.R
import com.example.calandfast.ui.AppViewModelProvider
import com.example.calandfast.ui.navigation.NavigationDestination

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
        WeeklyCalorieBody(
            uiState.value,
            weeklyCal = {viewModel.initThisWeek()},
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun WeeklyCalorieBody(
    uiState: WeeklyUiState,
    weeklyCal: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Button(
            onClick = weeklyCal
        ) { }
        Text(

            text= "Tuesday $weeklyCal"
        )
    }
}