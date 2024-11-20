package com.example.fattrack.view.scan

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.example.fattrack.R
import com.example.fattrack.databinding.ActivityCameraBinding
import java.io.File
import java.io.FileOutputStream

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
//    private var currentImageUri: Uri? = null
    private var flashMode: Int = ImageCapture.FLASH_MODE_OFF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gallery.setOnClickListener { startGallery() }
        // Ambil foto saat tombol capture ditekan
        binding.captureImage.setOnClickListener { takePhoto() }

        binding.flash.setOnClickListener { flashCamera() }

        // Mulai kamera pertama kali
        startCamera()

        // Atur tombol back untuk kembali ke halaman sebelumnya
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Ganti kamera saat tombol ditekan
        binding.switchCamera.setOnClickListener {
            // Ganti cameraSelector antara kamera depan dan belakang
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }

            // Restart kamera dengan selector baru
            startCamera()
        }

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            try {
                // Unbind semua use cases sebelum memulai ulang
                cameraProvider.unbindAll()

                // Buat ulang Preview dengan SurfaceProvider
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    }

                imageCapture = ImageCapture.Builder().build()

                // Bind kamera ke lifecycle Activity dengan selector yang diperbarui
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun createCustomTempFile(context: Context): File {
        val timeStamp =
            System.currentTimeMillis().toString() // Tambahkan timestamp untuk nama file unik
        val filesDir = context.externalCacheDir
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(this)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                    // Proses gambar untuk membuatnya mirror dan memastikan orientasi
                    val mirroredImageFile = processImageMirrorAndOrientation(photoFile)

                    // Kirim hasil ke ResultActivity dengan file baru
                    val mirroredPhotoUri = Uri.fromFile(mirroredImageFile)
                    goToResultActivity(mirroredPhotoUri)
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
        )
    }

    private fun processImageMirrorAndOrientation(photoFile: File): File {
        val originalBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
        val exif = ExifInterface(photoFile.absolutePath)

        // Ambil informasi orientasi EXIF
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val matrix = Matrix()

        // Terapkan rotasi berdasarkan orientasi EXIF
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        // Terapkan mirror (horizontal flip)
        matrix.preScale(1f, -1f)

        // Buat bitmap baru dengan rotasi dan flip
        val processedBitmap = Bitmap.createBitmap(
            originalBitmap,
            0,
            0,
            originalBitmap.width,
            originalBitmap.height,
            matrix,
            true
        )

        // Simpan bitmap yang telah diproses ke file baru
        val mirroredImageFile = createCustomTempFile(this)
        FileOutputStream(mirroredImageFile).use { out ->
            processedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }

        // Bebaskan memori dari bitmap yang tidak lagi digunakan
        originalBitmap.recycle()
        processedBitmap.recycle()

        return mirroredImageFile
    }





    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            // Kirim hasil ke ResultActivity
            goToResultActivity(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun goToResultActivity(imageUri: Uri) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("image_uri", imageUri.toString()) // Kirim URI sebagai string
        }
        startActivity(intent)
    }


    private fun flashCamera() {
        flashMode = if (flashMode == ImageCapture.FLASH_MODE_OFF) {
            ImageCapture.FLASH_MODE_ON
        } else {
            ImageCapture.FLASH_MODE_OFF
        }

        // Update flash mode di ImageCapture
        imageCapture?.flashMode = flashMode

        // Update ikon flash (opsional)
        val flashIcon = if (flashMode == ImageCapture.FLASH_MODE_OFF) {
            R.drawable.ic_flash_off
        } else {
            R.drawable.ic_flash
        }
        binding.flash.setImageResource(flashIcon)
    }

    companion object {
        private const val TAG = "CameraActivity"
    }
}
