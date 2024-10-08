package com.crty.ams.core.ui.compose.picker


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crty.ams.core.ui.theme.AmsTheme

@Composable
fun LoadingContainer(
    show: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    // Nothing. Just to block click from underlying elements.
                }
                .fillMaxSize()
                .background(Color.Black.copy(alpha = .6f))
        ) {
            Card(
                modifier = Modifier
                    .size(200.dp, 180.dp)
                    .align(Alignment.Center),
                elevation = CardDefaults.elevatedCardElevation()
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        Modifier.size(76.dp),
                        strokeWidth = 8.dp
                    )

                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = "Loading...",
                        fontSize = 26.sp
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun LoadingContainerPreviewDark() {
    AmsTheme {
        LoadingContainer(
            show = true
        )
    }
}
