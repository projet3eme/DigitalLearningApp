package com.example.digitallearningapp.utils

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.io.FileOutputStream

@Composable
fun rememberImagePicker(
    onImagePicked: (Uri) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val currentOnImagePicked by rememberUpdatedState(onImagePicked)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            // نسخ الصورة إلى ذاكرة التطبيق الداخلية لضمان بقائها للأبد
            val internalUri = saveImageToInternalStorage(context, selectedUri)
            internalUri?.let { currentOnImagePicked(it) }
        }
    }

    return remember {
        {
            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }
}

// دالة مساعدة لحفظ الصورة في مجلد التطبيق الخاص لضمان الديمومة
private fun saveImageToInternalStorage(context: Context, uri: Uri): Uri? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        // إنشاء ملف فريد لكل مرة أو استبدال ملف الملف الشخصي
        val file = File(context.filesDir, "profile_image_${System.currentTimeMillis()}.jpg")
        
        // حذف الملفات القديمة التي تبدأ بـ profile_image لتوفير المساحة
        context.filesDir.listFiles()?.forEach { 
            if (it.name.startsWith("profile_image") && it.name != file.name) {
                it.delete()
            }
        }

        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
