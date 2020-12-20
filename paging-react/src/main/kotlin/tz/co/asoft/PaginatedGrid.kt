@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

import kotlinext.js.jsObject
import kotlinx.html.DIV
import react.*
import styled.StyledDOMBuilder
import tz.co.asoft.Pager.State.Error
import tz.co.asoft.Pager.State.Loading
import tz.co.asoft.Pager.State.Showing

private external interface PaginatedGridProps<D> : RProps {
    var pager: Pager<D>
    var cols: String
    var gap: String
}

private fun <D> RBuilder.ShowGrid(
    page: Page<D>?,
    pager: Pager<D>,
    cols: String,
    gap: String,
    header: (StyledDOMBuilder<DIV>.() -> Unit)?,
    builder: StyledDOMBuilder<DIV>.(D?) -> Unit
) = Grid(rows = "30px 1fr", gap = "0.5em") {
    Paginator(
        onPrev = { pager.loadPrevious() }.takeIf { pager.canLoadPrevious() },
        center = header,
        onNext = { pager.loadNext() }.takeIf { pager.canLoadNext() }
    )
    GridAdapter(
        data = page?.data ?: List(pager.pageSize) { null },
        cols = cols,
        gap = gap,
        rows = "1fr",
        builder = builder
    )
}

private fun <D> PaginatedGridHOC(
    header: (StyledDOMBuilder<DIV>.() -> Unit)? = null,
    builder: StyledDOMBuilder<DIV>.(D?) -> Unit
) = functionalComponent<PaginatedGridProps<D>> { props ->
    when (val state = props.pager.state.asState()) {
        is Loading -> ShowGrid(state.cachedPage, props.pager, props.cols, props.gap, header, builder)
        is Showing -> ShowGrid(state.page, props.pager, props.cols, props.gap, header, builder)
        is Error -> Error(state.cause?.message ?: "Unknown error")
    }
}

fun <D> RBuilder.PaginatedGrid(
    fetcher: PageFetcher<D>,
    cols: String,
    gap: String = "1em",
    header: (StyledDOMBuilder<DIV>.() -> Unit)? = null,
    builder: StyledDOMBuilder<DIV>.(D?) -> Unit
): ReactElement = PaginatedGrid(fetcher.pager, cols, gap, header, builder)

fun <D> RBuilder.PaginatedGrid(
    pager: Pager<D>,
    cols: String,
    gap: String = "1em",
    header: (StyledDOMBuilder<DIV>.() -> Unit)? = null,
    builder: StyledDOMBuilder<DIV>.(D?) -> Unit
) = child(PaginatedGridHOC(header, builder), jsObject<PaginatedGridProps<D>>().also {
    it.pager = pager
    it.cols = cols
    it.gap = gap
})

fun <D> RBuilder.PaginatedList(
    fetcher: PageFetcher<D>,
    gap: String = "1em",
    header: (StyledDOMBuilder<DIV>.() -> Unit)? = null,
    builder: StyledDOMBuilder<DIV>.(D?) -> Unit
) = PaginatedGrid(fetcher.pager, cols = "1fr", gap = gap, header = header, builder = builder)

fun <D> RBuilder.PaginatedList(
    pager: Pager<D>,
    gap: String = "1em",
    header: (StyledDOMBuilder<DIV>.() -> Unit)? = null,
    builder: StyledDOMBuilder<DIV>.(D?) -> Unit
) = PaginatedGrid(pager, cols = "1fr", gap = gap, header = header, builder = builder)