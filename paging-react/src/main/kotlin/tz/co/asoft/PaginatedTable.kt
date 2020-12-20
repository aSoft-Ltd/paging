package tz.co.asoft

import kotlinext.js.jsObject
import react.RBuilder
import react.RProps
import react.functionalComponent

private external interface Props<D> : RProps {
    var pager: Pager<D>
    var columns: List<Column<D?>>
    var actions: List<AButton<D?>>?
    var sortable: Boolean
    var resizable: Boolean
    var filterable: Boolean
}

private fun <D> PaginatedTableHook() = functionalComponent<Props<D>> { props ->
    when (val ui = props.pager.state.asState()) {
        is Pager.State.Loading -> ShowTable(props, ui.cachedPage)
        is Pager.State.Showing -> ShowTable(props, ui.page)
        is Pager.State.Error -> Error(ui.cause?.message ?: "Unknown error")
    }
}

private fun <D> RBuilder.ShowTable(props: Props<D>, page: Page<D>?) = Grid(rows = "1fr 30px") {
    val pageSize = props.pager.pageSize
    FoldableTable(
        data = page?.data ?: List(pageSize) { jsObject() },
        columns = props.columns,
        actions = props.actions,
        showPagination = false,
        defaultPageSize = pageSize,
        sortable = props.sortable,
        resizable = props.resizable,
        filterable = props.filterable
    )
    Paginator(
        onPrev = { props.pager.loadPrevious() }.takeIf { props.pager.canLoadPrevious() },
        onNext = { props.pager.loadNext() }.takeIf { props.pager.canLoadNext() }
    )
}

inline fun <D> RBuilder.PaginatedTable(
    fetcher: PageFetcher<D>,
    columns: List<Column<D?>>,
    actions: List<AButton<D?>>? = null,
    sortable: Boolean = true,
    resizable: Boolean = true,
    filterable: Boolean = true
) = PaginatedTable(fetcher.pager, columns, actions, sortable, resizable, filterable)

fun <D> RBuilder.PaginatedTable(
    pager: Pager<D>,
    columns: List<Column<D?>>,
    actions: List<AButton<D?>>? = null,
    sortable: Boolean = true,
    resizable: Boolean = true,
    filterable: Boolean = true
) = child(PaginatedTableHook<D>(), jsObject<Props<D>>().also {
    it.pager = pager
    it.columns = columns
    it.actions = actions
    it.sortable = sortable
    it.resizable = resizable
    it.filterable = filterable
}) {}