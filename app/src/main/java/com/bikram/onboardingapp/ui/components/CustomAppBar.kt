package com.bikram.onboardingapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.bikram.onboardingapp.model.NavItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(selectedIndex: MutableState<Int>) {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = NavItems.fromInt(selectedIndex.value).name,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    )
}