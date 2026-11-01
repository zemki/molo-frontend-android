package news.molo.core.util.formdata

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

suspend fun Context.resolveFile(path: String): File? = withContext(Dispatchers.IO) {
    val uri = path.toUri()
    val resolver = contentResolver

    val fileName = getFileNameFromUri(resolver, uri) ?: return@withContext null

    resolver.openInputStream(uri)?.use { inputStream ->
        val tempFileName = "temp_${System.currentTimeMillis()}_$fileName"
        val tempFile = File(cacheDir, tempFileName)

        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        tempFile
    }
}

private fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String? {
    return contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (cursor.moveToFirst() && nameIndex >= 0) {
            cursor.getString(nameIndex)
        } else {
            null
        }
    }
}