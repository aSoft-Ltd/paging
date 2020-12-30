package tz.co.asoft

interface PagingSource<D> {
    val predicate: (D) -> Boolean
    fun prevOf(page: Page<D>): Later<Page<D>>
    fun nextOf(page: Page<D>): Later<Page<D>>
    fun firstPage(pageSize: Int): Later<Page<D>>
    fun pager(pageSize: Int): Pager<D> {
        val fetcher = PageFetcher(this, pageSize)
        return Pager(fetcher)
    }
}