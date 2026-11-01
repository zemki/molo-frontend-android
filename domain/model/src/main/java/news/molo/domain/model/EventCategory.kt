package news.molo.domain.model

data class EventCategory(
    val id: Int,
    val name: String,
    val order: Int,
    val eventCount: Int,
) {
    companion object {
        val mock = EventCategory(
            id = 1,
            name = "Event Category",
            order = 1,
            eventCount = 2
        )
    }
}
