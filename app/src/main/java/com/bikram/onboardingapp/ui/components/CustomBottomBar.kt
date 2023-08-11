package com.bikram.onboardingapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bikram.onboardingapp.data.NavItems

@Composable
fun CustomBottomBar(selectedIndex: MutableState<Int>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        NavigationBar {
            NavItems.values().forEachIndexed { index, _ ->
                val isSelected = selectedIndex.value == index
                NavigationBarItem(
                    icon = {
                        when (index) {
                            0 -> {
                                NavIcons(
                                    Icons.Filled.Home,
                                    isSelected
                                )
                            }

                            1 -> {
                                NavIcons(
                                    Icons.Filled.List,
                                    isSelected
                                )
                            }

                            2 -> {
                                NavIcons(
                                    Icons.Filled.Person,
                                    isSelected
                                )
                            }
                        }
                    },
                    selected = isSelected,
                    onClick = { selectedIndex.value = index }
                )
            }
        }
    }
}

@Composable
fun NavIcons(imageVector: ImageVector, isTintColor: Boolean) {
    if (isTintColor) {
        Image(
            modifier = Modifier.wrapContentSize(),
            imageVector = imageVector,
            colorFilter = ColorFilter.tint(Color.Black),
            contentScale = ContentScale.Fit,
            contentDescription = "tab_icon_selected"
        )
    } else {
        Image(
            modifier = Modifier.wrapContentSize(),
            imageVector = imageVector,
            colorFilter = ColorFilter.tint(Color.Gray),
            contentScale = ContentScale.Fit,
            contentDescription = "tab_icon_unselected"
        )
    }
}