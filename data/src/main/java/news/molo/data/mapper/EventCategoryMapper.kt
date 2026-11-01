package news.molo.data.mapper

import news.molo.data.network.response.EventCategoryResponse
import news.molo.domain.model.EventCategory


fun EventCategoryResponse.toEventCategory(): EventCategory = EventCategory(
    id = this.id,
    name = this.name,
    order = this.order,
    eventCount = this.eventCount
)