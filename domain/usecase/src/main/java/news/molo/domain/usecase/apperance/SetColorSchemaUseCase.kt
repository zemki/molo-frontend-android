package news.molo.domain.usecase.apperance

import de.convelop.forge.usecase.UseCase
import news.molo.domain.model.ColorSchemaPreference
import news.molo.domain.repository.SessionRepository

class SetColorSchemaUseCase(
    private val sessionRepository: SessionRepository,
) : UseCase<ColorSchemaPreference, Unit, String>() {

    override suspend fun UseCaseScope<String>.execute(params: ColorSchemaPreference) {
        sessionRepository.setColorSchema(params)
    }

    override fun mapError(t: Throwable): String {
        return t.message ?: ""
    }
}