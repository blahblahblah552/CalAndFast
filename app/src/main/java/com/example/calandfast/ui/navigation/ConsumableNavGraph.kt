package com.example.calandfast.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.calandfast.ui.home.HomeDestination
import com.example.calandfast.ui.home.HomeScreen
import com.example.calandfast.ui.consumable.ConsumableDetailsDestination
import com.example.calandfast.ui.consumable.ConsumableDetailsScreen
import com.example.calandfast.ui.consumable.ConsumableEditDestination
import com.example.calandfast.ui.consumable.ConsumableEditScreen
import com.example.calandfast.ui.consumable.ConsumableEntryDestination
import com.example.calandfast.ui.consumable.ConsumableEntryScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun ConsumableNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(ConsumableEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${ConsumableDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = ConsumableEntryDestination.route) {
            ConsumableEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ConsumableDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ConsumableDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ConsumableDetailsScreen(
                navigateToEditItem = { navController.navigate("${ConsumableEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = ConsumableEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ConsumableEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ConsumableEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}