package com.bikram.onboardingapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bikram.onboardingapp.R
import com.bikram.onboardingapp.ui.components.CustomSpacer
import com.bikram.onboardingapp.ui.components.CustomToast

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ProfileImage()
        CustomSpacer(20.dp)

        CustomProfileRow(text = "Account info", icon = Icons.Filled.List)
        CustomProfileRow(text = "Notifications", icon = Icons.Filled.Notifications)
        CustomProfileRow(text = "Settings", icon = Icons.Filled.Settings)
        CustomProfileRow(text = "Help center", icon = Icons.Filled.Info)
        CustomProfileRow(text = "Logout", icon = Icons.Filled.Close)

        CustomSpacer(50.dp)
    }
}

@Composable
private fun CustomProfileRow(text: String, icon: ImageVector) {
    val context = LocalContext.current
    val message = stringResource(id = R.string.coming_soon)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(5.dp)
            .clickable {
                CustomToast(message, context)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.weight(0.05f),
            tint = Color.White
        )
        Text(text, modifier = Modifier.weight(0.2f), color = Color.White)
        Icon(
            Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.weight(0.05f),
            tint = Color.White
        )
    }

    CustomSpacer(15.dp)
}

@Composable
private fun ProfileImage(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        contentAlignment = Alignment.Center
    ) {
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

@Preview(showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}