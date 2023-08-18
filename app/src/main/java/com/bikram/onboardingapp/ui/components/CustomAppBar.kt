package com.bikram.onboardingapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bikram.onboardingapp.data.NavItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(selectedIndex: MutableState<Int>) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = NavItems.fromInt(selectedIndex.value).name,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    )
}