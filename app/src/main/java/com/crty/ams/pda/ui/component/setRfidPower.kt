package com.crty.ams.pda.ui.component


import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SetRfidPowerSkeleton(
    power: Int,
    onPowerChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "设置RFID功率：$power",
                //text = stringResource(R.string.adjust_power, power),
                style = MaterialTheme.typography.titleMedium
            )
            Slider(
                value = power.toFloat(),
                onValueChange = { newValue -> onPowerChange(newValue.toInt()) },
                // 东集成 5-33
                valueRange = 5f..33f,
                steps = 29,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}




@Preview
@Composable
fun SetRfidPowerSkeletonPreview() {
    var power by remember { mutableIntStateOf(15) }
    SetRfidPowerSkeleton(power = power, onPowerChange = { power = it })
}