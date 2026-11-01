package news.molo.domain.usecase.article

import de.convelop.forge.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import news.molo.domain.repository.ArticleRepository

class ObserveArticleBookmarksUseCase(
    private val articleRepository: ArticleRepository
) : FlowUseCase<Unit, List<Int>>() {

    override fun execute(params: Unit): Flow<List<Int>> = articleRepository.observeBookmarkSelection()
}