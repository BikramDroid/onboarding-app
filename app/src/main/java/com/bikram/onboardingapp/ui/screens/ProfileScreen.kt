package com.bikram.onboardingapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bikram.onboardingapp.R

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp)
    )
    {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImage()
            Divider(modifier = Modifier.padding(30.dp))
            ProfileInfo()
        }
    }
}

@Composable
private fun ProfileInfo() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Username",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Subtitle",
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            text = "Some more details here..",
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun ProfileImage(modifier: Modifier = Modifier) {
    Surface(
        shape = CircleShape,
        border = BorderStroke(0.5.dp, Color.LightGray),
        shadowElevation = 5.dp
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_img),
            contentDescription = "profile image",
            modifier = modifier.size(130.dp),
            contentScale = ContentScale.Crop
        )
    }
}