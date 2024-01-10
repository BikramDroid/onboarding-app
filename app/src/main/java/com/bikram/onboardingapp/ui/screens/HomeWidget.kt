package com.bikram.onboardingapp.ui.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.material3.ColorProviders
import androidx.glance.text.Text
import com.bikram.onboardingapp.R
import com.bikram.onboardingapp.ui.theme.DarkColorScheme
import com.bikram.onboardingapp.ui.theme.LightColorScheme

object HomeWidget : GlanceAppWidget() {
    // Mandatory overrides
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme(colors = colorScheme) {
                Widget()
            }
        }
    }

    // Design your UI here
    @Composable
    private fun Widget() {
        Box(
            modifier = GlanceModifier
                .background(GlanceTheme.colors.background)
                .padding(15.dp)
                .cornerRadius(20.dp)
        ) {
            LazyColumn {
                items(5) {
                    Row(
                        modifier = GlanceModifier
                            .padding(
                                vertical = 10.dp,
                                horizontal = 5.dp
                            )
                            .clickable {
                                // Do whatever
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.arrow_outward),
                            contentDescription = null
                        )
                        Spacer(modifier = GlanceModifier.width(10.dp))
                        Text(text = "My first widget")
                    }
                }
            }
        }
    }
}

// Color scheme from Theme.kt
private val colorScheme = ColorProviders(
    light = LightColorScheme,
    dark = DarkColorScheme
)

// Actual class referred from manifest
class HomeWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = HomeWidget
}