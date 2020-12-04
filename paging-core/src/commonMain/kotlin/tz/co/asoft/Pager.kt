package tz.co.asoft

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Pager<D>(private val fetcher: PageFetcher<D>) : CoroutineScope by CoroutineScope(SupervisorJob()) {
    val state = MutableStateFlow(fetcher.state.value.toPagingState())

    val pageSize get() = fetcher.pageSize

    init {
        launch {
            fetcher.state.collect {
                state.value = it.toPagingState()
            }
        }
    }

    sealed class State<D> {
        class Loading<D>(val cachedPage: Page<D>?, val msg: String) : State<D>()
        class Showing<D>(val page: Page<D>) : State<D>()
        class Error<D>(val cause: Throwable?) : State<D>()
    }

    private fun PageFetcher.State<D>.toPagingState(): State<D> = when (this) {
        is PageFetcher.State.Idle.FromInitiation -> State.Loading(null, "Loading")
        is PageFetcher.State.Loading -> State.Loading(cachedPage, "Loading")
        is PageFetcher.State.Idle.FromSuccess -> State.Showing(page)
        is PageFetcher.State.Idle.FromFailure -> State.Error(cause)
    }

    fun canLoadNext() = fetcher.canLoadNext()
    fun loadNext() = fetcher.loadNext()
    fun canLoadPrevious() = fetcher.canLoadPrevious()
    fun loadPrevious() = fetcher.loadPrevious()
}