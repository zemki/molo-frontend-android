package news.molo.core.util.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import de.convelop.forge.paging.UseCasePagingFactory
import de.convelop.forge.usecase.FlowOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

fun <P: Any, O : Any> createPager(
    getItemsUseCase: FlowOutcomeUseCase<P, List<O>, MoloError>,
    paramsFactory: (limit: Int, offset: Int) -> P,
): Pager<Int, O> {

    return UseCasePagingFactory.create(
        config = PagingConfig(
            pageSize = PagingConstants.NETWORK_PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = PagingConstants.NETWORK_PAGE_SIZE,
        ),
        startingKey = 0,
        useCase = getItemsUseCase,
        paramsFactory = { pageKey, loadSize ->
            val page = pageKey ?: 0
            val offset = page * loadSize

            paramsFactory(loadSize, offset)
        },
        getItems = { successList -> successList },
        getNextKey = { data, currentKey ->
            if (data.isEmpty()) {
                null
            } else {
                currentKey + 1
            }
        },
        transformError = { error.message }
    )
}