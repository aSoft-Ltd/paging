package tz.co.asoft

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Pager<K : Any, D : Any>(
    private val fetcher: PageFetcher<K, D>,
    private val showTmpPages: Boolean = true
) : CoroutineScope by CoroutineScope(SupervisorJob()) {
    val state = MutableStateFlow(fetcher.state.value.toPagingState())

    init {
        launch {
            fetcher.state.collect {
                state.value = it.toPagingState()
            }
        }
    }

    private fun PageFetcher.State<K, D>.toPagingState(): PagingState<K, D> = when (this) {
        is PageFetcher.State.Idle.FromInitiation -> PagingState.Loading("Loading")
        is PageFetcher.State.Loading -> PagingState.Loading("Loading")
        is PageFetcher.State.Idle.FromSuccess -> PagingState.Showing(page)
        is PageFetcher.State.Idle.FromFailure -> PagingState.Error(cause)
    }

    fun loadNext() = fetcher.loadNext()

    fun loadPrevious() = fetcher.loadPrevious()
}