package news.molo.domain.usecase.search

import de.convelop.forge.usecase.UseCase
import news.molo.domain.model.SearchHistoryEntry
import news.molo.domain.repository.SearchHistoryRepository
import news.molo.domain.usecase.error.MoloError

class StoreSearchHistoryEntryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) : UseCase<SearchHistoryEntry, Unit, MoloError>() {
    override suspend fun UseCaseScope<MoloError>.execute(
        params: SearchHistoryEntry
    ) {
        catch(
            block = { searchHistoryRepository.insertSearchEntry(params) },
            mapper = { MoloError.SearchEntryCreationFailed }
        )
    }

    override fun mapError(t: Throwable): MoloError {
        return MoloError.SearchEntryCreationFailed
    }
}