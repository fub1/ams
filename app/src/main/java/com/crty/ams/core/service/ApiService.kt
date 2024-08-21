package com.crty.ams.core.service

import android.content.Context
import android.net.Uri
import com.crty.ams.core.ui.compose.camera2.getUriForFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

interface ApiService {
    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>
}

class ImageUploadService(private val context: Context) {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://your-api-url.com/") // 替换为你的API地址
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    suspend fun uploadImage(uri: Uri) {
        // 1. 获取文件名
        val fileName = getFileName(uri)

        // 2. 获取文件的输入流并转换为 RequestBody
        val inputStream = context.contentResolver.openInputStream(uri)
        val requestBody = inputStream?.readBytes()?.let {
            RequestBody.create(context.contentResolver.getType(uri)?.toMediaTypeOrNull(), it)
        }

        // 3. 将 RequestBody 包装为 MultipartBody.Part
        val multipartBody = requestBody?.let {
            MultipartBody.Part.createFormData("image", fileName, it)
        }

        // 4. 发送请求
        if (multipartBody != null) {
            val response = api.uploadImage(
                RequestBody.create("text/plain".toMediaTypeOrNull(), "Image Description"),
                multipartBody
            )

            // 处理响应
            if (response.isSuccessful) {
                // 请求成功处理
            } else {
                // 请求失败处理
            }
        }
    }

    // 获取文件名的辅助方法
    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndexOrThrow("_display_name"))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result ?: "unknown_file"
    }
}