package com.crty.ams.core.ui.component

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File

@Composable
fun ImageCaptureOrPickScreen(
    onImageCaptured: (Uri) -> Unit
) {
    val context = LocalContext.current
    var isCameraPermissionGranted by remember { mutableStateOf(false) }
    var isCapturing by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Request camera permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isCameraPermissionGranted = isGranted
    }.apply {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launch(Manifest.permission.CAMERA)
        }
    }

    // Camera preview and capture logic
    if (isCameraPermissionGranted) {
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
        var previewView by remember { mutableStateOf<PreviewView?>(null) }
        val lifecycleOwner = LocalLifecycleOwner.current
        val imageCapture = remember { ImageCapture.Builder().build() }

        LaunchedEffect(Unit) {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )

            preview.setSurfaceProvider(previewView?.surfaceProvider)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        previewView = this
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                isCapturing = true
                val photoFile = File.createTempFile("temp", ".jpg", context.cacheDir)
                imageCapture.takePicture(
                    ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            isCapturing = false
                            capturedImageUri = Uri.fromFile(photoFile)
                            onImageCaptured(capturedImageUri!!)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            isCapturing = false
                            // Handle error
                        }
                    }
                )
            }) {
                Text(text = if (isCapturing) "Capturing..." else "Capture Image")
            }
        }
    } else {
        Text("Camera permission is required.")
    }

    if (capturedImageUri != null) {
        // Handle captured image
    }
}