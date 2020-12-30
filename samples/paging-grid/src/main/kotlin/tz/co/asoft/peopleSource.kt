package tz.co.asoft

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay

fun peopleSource(people: List<Person>, pred: (Person) -> Boolean = { true }) = object : PagingSource<Person> {
    val scope = CoroutineScope(SupervisorJob())
    override val predicate: (Person) -> Boolean = pred

    override fun prevOf(page: Page<Person>) = scope.later {
        val pageNo = page.pageNo - 1
        delay(1000)
        val data = people.chunked(page.pageSize).getOrNull(pageNo - 1) ?: listOf()
        Page(
            data = data,
            pageSize = page.pageSize,
            pageNo = pageNo,
        )
    }

    override fun nextOf(page: Page<Person>) = scope.later {
        val pageNo = page.pageNo + 1
        delay(1000)
        val data = people.chunked(page.pageSize).getOrNull(pageNo - 1) ?: listOf()
        Page(
            data = data,
            pageSize = page.pageSize,
            pageNo = pageNo,
        )
    }

    override fun firstPage(pageSize: Int) = scope.later {
        val data = people.chunked(pageSize).getOrNull(0) ?: listOf()
        delay(1000)
        Page(
            data = data,
            pageSize = pageSize,
            pageNo = 1
        )
    }
}