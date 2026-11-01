package news.molo.android.navigation

import de.convelop.forge.navigation.NavigationDispatcher
import de.convelop.forge.navigation.NavigationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class ChannelNavigationDispatcher : NavigationDispatcher {
    private val _events = Channel<NavigationEvent>(Channel.BUFFERED)

    override val events: Flow<NavigationEvent> = _events.receiveAsFlow()

    override suspend fun dispatch(event: NavigationEvent) {
        _events.send(event)
    }
}