package com.bikram.onboardingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bikram.onboardingapp.ui.screens.MasterPage
import com.bikram.onboardingapp.ui.theme.OnboardingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnboardingAppTheme {
                // Home/master screen with a Scaffold for app
                // bar and bottom navigation tabs
                MasterPage()
            }
        }
    }
}