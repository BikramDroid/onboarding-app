package com.bikram.onboardingapp.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bikram.onboardingapp.R
import com.bikram.onboardingapp.ui.components.CustomSpacer
import com.pushpal.jetlime.data.JetLimeItemsModel
import com.pushpal.jetlime.data.config.IconAnimation
import com.pushpal.jetlime.data.config.IconType
import com.pushpal.jetlime.data.config.JetLimeItemConfig
import com.pushpal.jetlime.data.config.JetLimeViewConfig
import com.pushpal.jetlime.data.config.LineType
import com.pushpal.jetlime.ui.JetLimeView

@Composable
fun ReceiptsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaymentBubbles()

        Text(
            text = stringResource(id = R.string.receipts),
            modifier = Modifier.padding(top = 80.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium

        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .height(1.dp)
                .background(color = Color.Black)
        )

        AddReceiptsTimeline()
    }
}

@Composable
private fun PaymentBubbles() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 60.dp, top = 150.dp)
    )
    {
        val paid = getRandomRadius(false)
        val primaryColor = MaterialTheme.colorScheme.primary

        Text(
            modifier = Modifier
                .padding(top = 25.dp, end = 75.dp)
                .drawBehind {
                    drawCircle(
                        color = primaryColor,
                        radius = paid,
                        center = Offset(size.width / 2, size.height / 2),
                    )
                },
            text = " Paid \nkr ${paid.toInt() + 100}",
            color = Color.White,
            fontSize = 20.sp
        )

        val saved = getRandomRadius(true)

        Text(
            modifier = Modifier
                .padding(top = 100.dp)
                .drawBehind {
                    drawCircle(
                        color = Color(51, 119, 107),
                        radius = saved
                    )
                },
            text = "Saved \n kr ${saved.toInt() - 100}",
            color = Color.White
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AddReceiptsTimeline() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),

        ) {
        Icon(Icons.Filled.List, "receipts", tint = MaterialTheme.colorScheme.primary)

        Text(
            text = stringResource(R.string.shopping_trip),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }

    CustomSpacer()

    val jetLimeItemsModel = remember {
        JetLimeItemsModel(
            list = mutableStateListOf(
                JetLimeItemsModel.JetLimeItem(
                    title = "Samsung Monitor - 5990 kr",
                    description = "Yesterday at 11.10",
                    jetLimeItemConfig = JetLimeItemConfig(
                        itemHeight = 80.dp,
                        iconType = IconType.Filled,
                        iconAnimation = IconAnimation()
                    )
                )
            )
        )
    }
    jetLimeItemsModel.addItem(
        JetLimeItemsModel.JetLimeItem(
            title = "Jacket - 559.90 kr",
            description = "Saturday at 15.55"
        )
    )

    jetLimeItemsModel.addItem(
        JetLimeItemsModel.JetLimeItem(
            title = "Hard disk - 640 kr",
            description = "1st Sep at 15.55"
        )
    )

    JetLimeView(
        modifier = Modifier
            .background(color = Color.White)
            .padding(15.dp),
        jetLimeItemsModel = jetLimeItemsModel,
        jetLimeViewConfig = JetLimeViewConfig(
            lineType = LineType.Solid,
            backgroundColor = MaterialTheme.colorScheme.primary
        ),
        listState = rememberLazyListState(),

        )
}

private fun getRandomRadius(saved: Boolean): Float {
    val res = if (saved)
        (175..200).random()
    else
        (300..325).random()

    return res.toFloat()
}

@Preview(showSystemUi = true)
@Composable
fun ReceiptsScreenPreview() {
    ReceiptsScreen()
}