package com.crty.ams.pda.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import com.crty.ams.pda.utils.deviceinfo.getAndroidId
import com.crty.ams.pda.utils.deviceinfo.getDeviceModel
import com.crty.ams.pda.utils.deviceinfo.getDeviceSN
import com.crty.ams.pda.utils.deviceinfo.getBrand

@Composable
fun ShowDeviceInfoScreen() {
    val context = LocalContext.current
    Column {
        Text(
            text = "Device Information",
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Brand:${getBrand()}",
                fontSize = 5.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Model:${getDeviceModel()}",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 5.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "SN:${getDeviceSN()}",
                fontSize = 7.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "AndroidId:${getAndroidId(context)}",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 5.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}