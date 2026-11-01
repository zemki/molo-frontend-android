package news.molo.data.storage

import android.content.Context
import news.molo.core.util.formdata.resolveFile
import news.molo.domain.provider.FileProvider
import java.io.File

class LocalFileProvider(
    private val context: Context
) : FileProvider {
    override suspend fun resolveFile(path: String): Result<File> {
        val file = context.resolveFile(path)
        return if (file != null) {
            Result.success(file)
        } else {
            Result.failure(Exception("File not found"))
        }
    }
}