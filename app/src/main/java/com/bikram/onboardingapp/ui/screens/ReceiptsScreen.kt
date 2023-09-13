package com.bikram.onboardingapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bikram.onboardingapp.R

@Composable
fun ReceiptsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 60.dp, top = 150.dp)
        )
        {
            val paid = getRandomRadius(false)

            Text(
                modifier = Modifier
                    .padding(end = 70.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.Blue,
                            radius = paid
                        )
                    },
                text = " Paid \nkr ${paid.toInt() + 100}",
                color = Color.White
            )

            val saved = getRandomRadius(true)

            Text(
                modifier = Modifier
                    .padding(top = 100.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.DarkGray,
                            radius = saved
                        )
                    },
                text = "Saved \n kr ${saved.toInt() - 100}",
                color = Color.White
            )
        }

        Text(
            text = stringResource(id = R.string.receipts),
            modifier = Modifier.padding(top = 80.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .height(1.dp)
                .background(color = Color.Black)
        )
    }
}

private fun getRandomRadius(saved: Boolean): Float {
    val res = if (saved)
        (175..200).random()
    else
        (300..350).random()

    return res.toFloat()
}

@Preview(showSystemUi = true)
@Composable
fun ReceiptsScreenPreview() {
    ReceiptsScreen()
}