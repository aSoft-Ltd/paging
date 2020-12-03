package tz.co.asoft

interface PagingSource<D> {
    val predicate: (D) -> Boolean
    suspend fun prevOf(page: Page<D>): Page<D>
    suspend fun nextOf(page: Page<D>): Page<D>
    suspend fun firstPage(pageSize: Int): Page<D>
    fun pager(pageSize: Int): Pager<D> {
        val fetcher = PageFetcher(this, pageSize)
        return Pager(fetcher)
    }
}