package tz.co.asoft

import kotlinext.js.jsObject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import react.*
import tz.co.asoft.PaginatedTable.Props
import tz.co.asoft.PaginatedTable.State

@JsExport
class PaginatedTable<D> private constructor(p: Props<D>) : Component<Props<D>, State<D>>(p) {
    class Props<D>(
        val pager: Pager<D>,
        val columns: List<Column<D?>>,
        val actions: List<AButton<D?>>?,
        val sortable: Boolean,
        val resizable: Boolean,
        val filterable: Boolean
    ) : RProps

    class State<D>(var value: Pager.State<D>) : RState

    init {
        state = State(p.pager.state.value)
    }

    private var observer: Job? = null
    override fun componentWillMount() {
        observer = props.pager.observe()
    }

    private fun Pager<D>.observe() = launch {
        state.collect {
            setState { value = it }
        }
    }

    override fun componentWillReceiveProps(nextProps: Props<D>) {
        observer?.cancel()
        observer = nextProps.pager.observe()
    }

    override fun componentWillUnmount() {
        observer?.cancel()
        cancel()
    }

    private fun RBuilder.ShowTable(page: Page<D>?) = Grid(rows = "1fr 30px") {
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

    override fun RBuilder.render(): dynamic = when (val ui = state.value) {
        is Pager.State.Loading -> ShowTable(ui.cachedPage)
        is Pager.State.Showing -> ShowTable(ui.page)
        is Pager.State.Error -> Error(ui.cause?.message ?: "Unknown error")
    }
}

fun <D> RBuilder.PaginatedTable(
    fetcher: PageFetcher<D>,
    columns: List<Column<D?>>,
    actions: List<AButton<D?>>? = null,
    sortable: Boolean = true,
    resizable: Boolean = true,
    filterable: Boolean = true
) = child(PaginatedTable::class.js, Props(fetcher.pager, columns, actions, sortable, resizable, filterable)) {}

fun <D> RBuilder.PaginatedTable(
    pager: Pager<D>,
    columns: List<Column<D?>>,
    actions: List<AButton<D?>>? = null,
    sortable: Boolean = true,
    resizable: Boolean = true,
    filterable: Boolean = true
) = child(PaginatedTable::class.js, Props(pager, columns, actions, sortable, resizable, filterable)) {}