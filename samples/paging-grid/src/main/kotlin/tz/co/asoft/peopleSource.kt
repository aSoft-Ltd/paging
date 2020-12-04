package tz.co.asoft

import kotlinx.coroutines.delay

fun peopleSource(people: List<Person>, pred: (Person) -> Boolean = { true }) = object : PagingSource<Person> {
    override val predicate: (Person) -> Boolean = pred

    override suspend fun prevOf(page: Page<Person>): Page<Person> {
        val pageNo = page.pageNo - 1
        delay(1000)
        val data = people.chunked(page.pageSize).getOrNull(pageNo - 1) ?: listOf()
        return Page(
            data = data,
            pageSize = page.pageSize,
            pageNo = pageNo,
        )
    }

    override suspend fun nextOf(page: Page<Person>): Page<Person> {
        val pageNo = page.pageNo + 1
        delay(1000)
        val data = people.chunked(page.pageSize).getOrNull(pageNo - 1) ?: listOf()
        return Page(
            data = data,
            pageSize = page.pageSize,
            pageNo = pageNo,
        )
    }

    override suspend fun firstPage(pageSize: Int): Page<Person> {
        val data = people.chunked(pageSize).getOrNull(0) ?: listOf()
        delay(1000)
        return Page(
            data = data,
            pageSize = pageSize,
            pageNo = 1
        )
    }
}