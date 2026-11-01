package news.molo.domain.provider

import java.io.File

interface FileProvider {
    suspend fun resolveFile(path: String): Result<File>
}