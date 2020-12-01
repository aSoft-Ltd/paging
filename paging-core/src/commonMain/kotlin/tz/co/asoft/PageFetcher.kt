package tz.co.asoft

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class PageFetcher<K : Any, D : Any>(
    private val loader: PageLoader<K, D>,
    private val pageSize: Int
) : CoroutineScope by CoroutineScope(SupervisorJob()) {
    val state = MutableStateFlow<State<K, D>>(State.Idle.FromInitiation())

    private var latestDisplayedPage: Page<K, D>? = null

    private var job: Job? = null

    init {
        loadFirst()
    }

    sealed class State<K : Any, D : Any> {
        class Loading<K : Any, D : Any> : State<K, D>()
        sealed class Idle<K : Any, D : Any> : State<K, D>() {
            class FromInitiation<K : Any, D : Any> : Idle<K, D>()
            class FromSuccess<K : Any, D : Any>(val page: Page<K, D>) : Idle<K, D>()
            class FromFailure<K : Any, D : Any>(val cause: Throwable?) : Idle<K, D>()
        }
    }

    private fun loadFirst() {
        job?.cancel()
        job = launch {
            flow<State<K, D>> {
                emit(State.Loading())
                val pageNode = loader.firstPage(pageSize)
                emit(State.Idle.FromSuccess(pageNode))
                latestDisplayedPage = pageNode
            }.catch {
                emit(State.Idle.FromFailure(it.cause))
            }.collect {
                state.value = it
            }
        }
    }

    fun loadNext() {
        job?.cancel()
        job = launch {
            flow<State<K, D>> {
                val currentPage =
                    latestDisplayedPage ?: throw Exception("Current page can't be null")
                emit(State.Loading())
                val page = loader.nextOf(currentPage)
                emit(State.Idle.FromSuccess(page))
                latestDisplayedPage = page
            }.catch {
                emit(State.Idle.FromFailure(it.cause))
            }.collect {
                state.value = it
            }
        }
    }

    fun loadPrevious() {
        job?.cancel()
        job = launch {
            flow<State<K, D>> {
                val current = latestDisplayedPage ?: throw Exception("Can't go previous")
                emit(State.Loading())
                val page = loader.prevOf(current)
                emit(State.Idle.FromSuccess(page))
                latestDisplayedPage = page
            }.catch {
                emit(State.Idle.FromFailure(it.cause))
            }.collect { state.value = it }
        }
    }
}