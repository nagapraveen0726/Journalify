package uk.ac.tees.mad.journalify.presentation.screen.camera

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executor

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CameraCaptureScreen(
    viewModel: CameraCaptureViewModel = hiltViewModel(),
    onCaptured: (String) -> Unit = {},
    onCancel: () -> Unit = {},
) {
    val ctx = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val imageCapture = remember { ImageCapture.Builder().build() }
    val previewView = remember { PreviewView(ctx) }
    val executor: Executor = ctx.mainExecutor

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) onCancel()
    }


    // Bind camera
    LaunchedEffect(Unit) {
        launcher.launch(android.Manifest.permission.CAMERA)

        val provider = ProcessCameraProvider.getInstance(ctx).get()

        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(previewView.surfaceProvider)

        provider.unbindAll()
        provider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            imageCapture
        )
    }

    // observe saved result
    LaunchedEffect(Unit) {
        viewModel.photo.collect { path ->
            onCaptured(path)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { previewView }
        )

        FloatingActionButton(
            onClick = {
                val file = File(
                    ctx.cacheDir,
                    "photo_${System.currentTimeMillis()}.jpg"
                )

                val output = ImageCapture.OutputFileOptions.Builder(file).build()

                imageCapture.takePicture(
                    output,
                    executor,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            scope.launch {
                                viewModel.save(file)
                            }
                        }

                        override fun onError(exc: ImageCaptureException) {
                            Log.e("Camera", "Capture failed", exc)
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) {
            Icon(Icons.Default.Add, null)
        }
    }
}
