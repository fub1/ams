/*
 * Copyright 2021 Md. Mahmudul Hasan Shohag
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ------------------------------------------------------------------------
 *
 * Project: Why Not Compose!
 * Developed by: @ImaginativeShohag
 *
 * Md. Mahmudul Hasan Shohag
 * imaginativeshohag@gmail.com
 *
 * Source: https://github.com/ImaginativeShohag/Why-Not-Compose
 */

package org.imaginativeworld.whynotcompose.ui.screens.tutorial.captureimageandcrop

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.crty.ams.MainActivity
import com.crty.ams.R
import com.crty.ams.core.service.ImageUploadService
import com.crty.ams.core.ui.compose.camera2.AppComponent
import com.crty.ams.core.ui.compose.camera2.createImageFile
import com.crty.ams.core.ui.compose.camera2.getUriForFile
import com.crty.ams.core.ui.compose.camera2.rememberImagePainter
import com.crty.ams.core.ui.compose.camera2.toast
import java.io.File
import java.util.Date
//import org.imaginativeworld.whynotcompose.utils.SquireCropImage
import com.crty.ams.core.ui.theme.AmsTheme
import kotlinx.coroutines.launch

@Composable
fun CaptureImageAndCropScreen(
    viewModel: CaptureImageAndCropViewModel,
    goBack: () -> Unit
) {
    val context = LocalContext.current

    // ----------------------------------------------------------------

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var f: File? = null


    val service = ImageUploadService(context)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                (context as? MainActivity)?.lifecycleScope?.launch {
                    if (f != null) {
                        service.uploadImage(f!!.getUriForFile(context))
                    } // 传入Uri对象
                }
            }
        }
    )

    // 创建一个 ActivityResultLauncher，用于拍照
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // 确保图片文件存在并读取
                imageUri?.let { uri ->
                    val tempFile = File(context.cacheDir, "temp_image_file_${Date().time}")
                    val tempUri = Uri.fromFile(tempFile)

                    // 复制图片到临时文件
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        context.contentResolver.openOutputStream(tempUri)?.use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    // 设置图片 URI
                    imageUri = tempUri

                    // 上传或处理图片
                    viewModel.uploadPhoto(
                        context = context,
                        imageUri = tempUri
                    )
                }
            } else {
                context.toast("Cannot save the image!")
            }
        }

    // ----------------------------------------------------------------

    CaptureImageAndCropScreenSkeleton(
        goBack = goBack,
        imagePath = imageUri,
        onChooseImageClicked = {
            f = context.createImageFile()
            val newPhotoUri = f!!.getUriForFile(context)

            imageUri = newPhotoUri

            cameraLauncher.launch(newPhotoUri)
        }
    )
}

@Preview
@Composable
fun CaptureImageAndCropScreenSkeletonPreview() {
    AmsTheme {
        CaptureImageAndCropScreenSkeleton()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CaptureImageAndCropScreenSkeletonPreviewDark() {
    AmsTheme {
        CaptureImageAndCropScreenSkeleton()
    }
}

@Composable
fun CaptureImageAndCropScreenSkeleton(
    goBack: () -> Unit = {},
    imagePath: Uri? = null,
    onChooseImageClicked: () -> Unit = {}
) {
    Scaffold(
        Modifier
            .navigationBarsPadding()
            .imePadding()
            .statusBarsPadding()
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
//            AppComponent.Header(
//                "Capture Image and Crop for Upload",
//                goBack = goBack
//            )

            // ----------------------------------------------------------------
            // ----------------------------------------------------------------

            Divider()

            // ----------------------------------------------------------------

            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 32.dp, top = 32.dp, end = 32.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                painter = rememberImagePainter(
                    data = imagePath,
                    placeholder = R.drawable.default_placeholder
                ),
                contentDescription = "Image",
                contentScale = ContentScale.Crop
            )

            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp),
                onClick = {
                    onChooseImageClicked()
                }
            ) {
                Text("Capture Image")
            }

            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp),
                onClick = {
                    
                }
            ) {
                Text("发请求")
            }

            // ----------------------------------------------------------------
            // ----------------------------------------------------------------

            AppComponent.BigSpacer()
        }
    }
}

@Preview
@Composable
fun captureImageAndCropScreenPreview() {
    val viewModel = CaptureImageAndCropViewModel()
    AmsTheme {
        CaptureImageAndCropScreen(
            viewModel = viewModel,
            goBack = {}
        )
    }
}
