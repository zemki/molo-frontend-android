package news.molo.domain.usecase.area

import de.convelop.forge.usecase.UseCase
import news.molo.domain.model.AreaWithRadius
import news.molo.domain.repository.SessionRepository

class GetSelectedAreaWithRadiusUseCase(
    private val sessionRepository: SessionRepository
) : UseCase<Unit, AreaWithRadius, String>() {

    override suspend fun UseCaseScope<String>.execute(params: Unit): AreaWithRadius {
        val areaWithRadius = sessionRepository.getSelectedLocationWithRadius().getOrNull()

        ensure(areaWithRadius != null) { "Keine Auswahl vorhanden." }
        // Maybe write a contract instead of force unwrap?
        return areaWithRadius!!
    }

    override fun mapError(t: Throwable): String {
        return t.message ?: ""
    }
}