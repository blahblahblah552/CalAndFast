package com.example.calandfast.ui.consumable

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calandfast.InventoryTopAppBar
import com.example.calandfast.R
import com.example.calandfast.database.Consumable
import com.example.calandfast.ui.AppViewModelProvider
import com.example.calandfast.ui.navigation.NavigationDestination
import com.example.calandfast.ui.theme.CalAndFastTheme
import kotlinx.coroutines.launch

object ConsumableEditDestination : NavigationDestination {
    override val route = "item_edit"
    override val titleRes = R.string.edit_item_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumableEditScreen (
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ConsumableEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
    ) {
        val coroutineScope = rememberCoroutineScope()
        Scaffold(
            topBar = {
                InventoryTopAppBar(
                    title = stringResource(ConsumableEditDestination.titleRes),
                    canNavigateBack = true,
                    navigateUp = onNavigateUp
                )
            },
            modifier = modifier
        ) { innerPadding ->
            ItemEntryBody(
                itemUiState = viewModel.consumableUiState,
                onItemValueChange = viewModel::updateUiState,
                onSaveClick = {
                    // Note: If the user rotates the screen very fast, the operation may get cancelled
                    // and the item may not be updated in the Database. This is because when config
                    // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                    // be cancelled - since the scope is bound to composition.
                    coroutineScope.launch {
                        viewModel.updateItem()
                        navigateBack()
                    }
                },
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

    @Preview(showBackground = true)
    @Composable
    fun ItemEditScreenPreview() {
        CalAndFastTheme {
            ConsumableEditScreen(navigateBack = { /*Do nothing*/ }, onNavigateUp = { /*Do nothing*/ })
        }
    }