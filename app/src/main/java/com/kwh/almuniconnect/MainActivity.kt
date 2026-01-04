package com.kwh.almuniconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kwh.almuniconnect.ui.theme.AlumniConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlumniConnectTheme {
                AppNavGraph(startDestination = Routes.SPLASH)
            }
        }


                }
            }
