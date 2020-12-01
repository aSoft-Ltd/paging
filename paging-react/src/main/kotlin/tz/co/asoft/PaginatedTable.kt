package tz.co.asoft

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import react.*
import tz.co.asoft.PaginatedTable.Props
import tz.co.asoft.PaginatedTable.State

@JsExport
class PaginatedTable<D : Any> private constructor(p: Props<D>) : RComponent<Props<D>, State<D>>(p),
    CoroutineScope by CoroutineScope(SupervisorJob()) {
    class Props<D : Any>(
        val pager: Pager<*, D>,
        val columns: List<Column<D>>,
        val actions: List<AButton<D>>?,
        val defaultPageSize: Int,
        val sortable: Boolean,
        val resizable: Boolean,
        val filterable: Boolean
    ) : RProps

    class State<D : Any>(var value: PagingState<*, D>) : RState

    init {
        state = State(p.pager.state.value)
    }

    private var observer: Job? = null
    override fun componentWillMount() {
        observer = props.pager.observe()
    }

    private fun Pager<*, D>.observe() = launch {
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

    override fun RBuilder.render(): dynamic = when (val ui = state.value) {
        is PagingState.Loading -> Loader(ui.msg)
        is PagingState.Showing -> Grid(rows = "auto") {
            FoldableTable(
                data = ui.page.data,
                columns = props.columns,
                actions = props.actions,
                showPagination = false,
                defaultPageSize = props.defaultPageSize,
                sortable = props.sortable,
                resizable = props.resizable,
                filterable = props.filterable
            )
            Paginator(
                onPrev = { props.pager.loadPrevious() }.takeIf { ui.page.prev != null },
                onNext = { props.pager.loadNext() }.takeIf { ui.page.nextKey != null }
            )
        }
        is PagingState.Error -> Error(ui.cause?.message ?: "Unknown error")
    }
}

fun <D : Any> RBuilder.PaginatedTable(
    pager: Pager<*, D>,
    columns: List<Column<D>>,
    actions: List<AButton<D>>? = null,
    defaultPageSize: Int = 15,
    sortable: Boolean = true,
    resizable: Boolean = true,
    filterable: Boolean = true
) = child(
    PaginatedTable::class.js,
    Props(pager, columns, actions, defaultPageSize, sortable, resizable, filterable)
) {}