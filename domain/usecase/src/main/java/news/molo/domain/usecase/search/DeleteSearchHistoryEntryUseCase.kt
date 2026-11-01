package news.molo.domain.usecase.search

import de.convelop.forge.usecase.UseCase
import news.molo.domain.repository.SearchHistoryRepository
import news.molo.domain.usecase.error.MoloError


class DeleteSearchHistoryEntryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) : UseCase<Int, Unit, MoloError>() {
    override suspend fun UseCaseScope<MoloError>.execute(
        params: Int
    ) {
        catch(
            block = { searchHistoryRepository.deleteSearchEntry(params) },
            mapper = { MoloError.SearchEntryDeletionFailed }
        )
    }

    override fun mapError(t: Throwable): MoloError {
        return MoloError.SearchEntryDeletionFailed
    }
}