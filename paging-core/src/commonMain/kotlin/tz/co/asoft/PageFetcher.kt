package tz.co.asoft

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class PageFetcher<D>(
    val source: PagingSource<D>,
    val pageSize: Int
) {
    val state = MutableStateFlow<State<D>>(State.Idle.FromInitiation())

    internal val scope = CoroutineScope(SupervisorJob())

    private var currentPageNo = -1

    private val pages = mutableMapOf<Int, Page<D>>()

    private var job: Job? = null

    init {
        loadFirst()
    }

    val pager get() = Pager(this)

    sealed class State<D> {
        class Loading<D>(val cachedPage: Page<D>?) : State<D>()
        sealed class Idle<D> : State<D>() {
            class FromInitiation<D> : Idle<D>()
            class FromSuccess<D>(val page: Page<D>) : Idle<D>()
            class FromFailure<D>(val cause: Throwable?) : Idle<D>()
        }
    }

    private fun MutableMap<Int, Page<D>>.update(page: Page<D>): Page<D> {
        currentPageNo = page.pageNo
        this[currentPageNo] = page
        return page; //put(page.pageNo, page) ?: throw RuntimeException("Failed to put page no ${page.pageNo} into cache")
    }

    private fun loadFirst() {
        job?.cancel()
        job = scope.launch {
            flow {
                emit(State.Loading(pages[1]))
                emit(State.Idle.FromSuccess(pages.update(source.firstPage(pageSize).await())))
            }.catch {
                emit(State.Idle.FromFailure(it.cause))
            }.collect {
                state.value = it
            }
        }
    }

    fun canLoadNext() = pages[currentPageNo]?.data?.size == pageSize

    fun loadNext() {
        job?.cancel()
        job = scope.launch {
            flow {
                val currentPage = pages[currentPageNo] ?: throw RuntimeException("Can't load next of unknow page")
                val cachedPage = pages[currentPageNo + 1]?.let { pages.update(it) }
                emit(State.Loading(cachedPage))
                emit(State.Idle.FromSuccess(pages.update(source.nextOf(currentPage).await())))
            }.catch {
                emit(State.Idle.FromFailure(it.cause))
            }.collect {
                state.value = it
            }
        }
    }

    fun canLoadPrevious(): Boolean = currentPageNo > 1

    fun loadPrevious() {
        job?.cancel()
        job = scope.launch {
            flow {
                val currentPage = pages[currentPageNo] ?: throw RuntimeException("Can't load previous of an unknown page")
                val cachedPage = pages[currentPage.pageNo - 1]?.let { pages.update(it) }
                emit(State.Loading(cachedPage))
                emit(State.Idle.FromSuccess(pages.update(source.prevOf(currentPage).await())))
            }.catch {
                emit(State.Idle.FromFailure(it.cause))
            }.collect { state.value = it }
        }
    }
}