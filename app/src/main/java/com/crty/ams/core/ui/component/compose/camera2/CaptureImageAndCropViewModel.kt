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

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.crty.ams.MainActivity
import com.crty.ams.core.service.ImageUploadService
import com.crty.ams.core.ui.compose.camera2.RealPathUtil
import com.crty.ams.core.ui.compose.camera2.createImageFile
import com.crty.ams.core.ui.compose.camera2.getUriForFile
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

@HiltViewModel
class CaptureImageAndCropViewModel @Inject constructor() : ViewModel() {

    fun uploadPhoto(
        context: Context,
        imageUri: Uri
    ) = viewModelScope.launch {
        val imagePath: String = RealPathUtil.getRealPath(context, imageUri) ?: return@launch

        // `imagePath` is the real path of the image.

        // Sample code for Retrofit:

        var imagePart: MultipartBody.Part? = imagePath.let { path ->
            val pictureFile = File(path)
            val requestBody: RequestBody =
                pictureFile.asRequestBody(path.toMediaTypeOrNull())

            MultipartBody.Part.createFormData(
                "photo",
                pictureFile.name,
                requestBody
            )
        }

        // Now use the `imagePart` in your request.

        // Example request:

        // @Multipart
        // @POST("api/v1/update_photo")
        // suspend fun updateProfilePhoto(
        //     @Header("Authorization") token: String?,
        //     @Part image: MultipartBody.Part?
        // ): Response<ProfileResponse>

        // Here, `imagePart` will be the `image` parameter.
    }

    private var imageFile: File? = null

    fun createImageFile(context: Context) {
        imageFile = context.createImageFile()
    }

    fun getUriForFile(context: Context): Uri? {
        return imageFile?.getUriForFile(context)
    }

//    val service = ImageUploadService(LocalContext.current)
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent(),
//        onResult = { uri ->
//            if (uri != null) {
//                (LocalContext.current as? MainActivity)?.lifecycleScope?.launch {
//
//                    imageFile?.let { service.uploadImage(it.getUriForFile(LocalContext.current)) }
//                }
//            }
//        }
//    )
}
