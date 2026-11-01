package news.molo.domain.usecase.search

import de.convelop.forge.usecase.UseCase
import news.molo.domain.repository.SearchHistoryRepository
import news.molo.domain.usecase.error.MoloError


class DeleteWholeSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) : UseCase<Unit, Unit, MoloError>() {
    override suspend fun UseCaseScope<MoloError>.execute(
        params: Unit
    ) {
        catch(
            block = { searchHistoryRepository.deleteWholeSearchHistory() },
            mapper = { MoloError.SearchEntryDeletionFailed }
        )
    }

    override fun mapError(t: Throwable): MoloError {
        return MoloError.SearchEntryDeletionFailed
    }
}