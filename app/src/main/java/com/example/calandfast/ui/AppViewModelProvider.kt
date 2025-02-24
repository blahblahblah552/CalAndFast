package com.example.calandfast.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.calandfast.ConsumableApplication
import com.example.calandfast.ui.consumable.ConsumableDetailsViewModel
import com.example.calandfast.ui.consumable.ConsumableEditViewModel
import com.example.calandfast.ui.consumable.ConsumableEntryViewModel
import com.example.calandfast.ui.home.HomeViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            ConsumableEditViewModel(
                this.createSavedStateHandle(),
                consumableApplication().container.consumablesRepository
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            ConsumableEntryViewModel(consumableApplication().container.consumablesRepository)
        }

        // Initializer for ItemDetailsViewModel
        initializer {
            ConsumableDetailsViewModel(
                this.createSavedStateHandle(),
                consumableApplication().container.consumablesRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(consumableApplication().container.consumablesRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [ConsumableApplication].
 */
fun CreationExtras.consumableApplication(): ConsumableApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ConsumableApplication)