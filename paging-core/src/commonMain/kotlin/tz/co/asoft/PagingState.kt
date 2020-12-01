package tz.co.asoft

sealed class PagingState<K : Any, D : Any> {
    class Loading<K : Any, D : Any>(val msg: String) : PagingState<K, D>()
    class Showing<K : Any, D : Any>(val page: Page<K, D>) : PagingState<K, D>()
    class Error<K : Any, D : Any>(val cause: Throwable?) : PagingState<K, D>()
}