package com.example.calandfast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.calandfast.ui.theme.CalAndFastTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CalAndFastTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ConsumableApp()
                }
            }
        }
    }
}
