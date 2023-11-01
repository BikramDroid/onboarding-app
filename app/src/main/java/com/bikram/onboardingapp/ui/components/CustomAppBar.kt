package com.bikram.onboardingapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bikram.onboardingapp.R
import com.bikram.onboardingapp.model.NavItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
    selectedIndex: MutableState<Int>,
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    searchButton: () -> Unit
) {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text =
                    if (canNavigateBack)
                        currentScreen
                    else
                        NavItems.fromInt(selectedIndex.value).name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            if (!canNavigateBack) {
                IconButton(onClick = searchButton ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(R.string.search_button),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}