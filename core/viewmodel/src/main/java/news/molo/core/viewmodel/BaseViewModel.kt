package news.molo.core.viewmodel

import de.convelop.forge.event.EventBus
import de.convelop.forge.navigation.NavigationDispatcher
import de.convelop.forge.viewmodel.NavResultAwareStateViewModel
import de.convelop.forge.viewmodel.StateViewModel
import news.molo.core.event.UIEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

abstract class BaseViewModel<State : Any, Intent> : StateViewModel<State, Intent, UIEvent> {
    constructor(initialState: State, eventBus: EventBus<UIEvent>?) : super(
        initialState = initialState,
        eventBus = eventBus,
        navigationDispatcher = resolveNavigationDispatcher(),
    )

    constructor(initialState: State, useEventBus: Boolean = true) : this(
        initialState = initialState,
        eventBus = if (useEventBus) resolveEventBus() else null,
    )

    protected fun showSnackbar(message: String) {
        sendUIEvent(UIEvent.SnackBar(message))
    }

    private companion object Companion : KoinComponent {
        fun resolveEventBus(): EventBus<UIEvent> = get()

        fun resolveNavigationDispatcher(): NavigationDispatcher = get()
    }
}

abstract class BaseViewModelWithNavResult<State : Any, Intent, NavResult : Any> :
    NavResultAwareStateViewModel<State, Intent, UIEvent, NavResult> {
    constructor(initialState: State, eventBus: EventBus<UIEvent>?) : super(
        initialState = initialState,
        eventBus = eventBus,
        navigationDispatcher = resolveNavigationDispatcher(),
    )

    constructor(initialState: State, useEventBus: Boolean = true) : this(
        initialState = initialState,
        eventBus = if (useEventBus) resolveEventBus() else null,
    )

    protected fun showSnackbar(message: String) {
        sendUIEvent(UIEvent.SnackBar(message))
    }

    private companion object Companion : KoinComponent {
        fun resolveEventBus(): EventBus<UIEvent> = get()

        fun resolveNavigationDispatcher(): NavigationDispatcher = get()
    }
}