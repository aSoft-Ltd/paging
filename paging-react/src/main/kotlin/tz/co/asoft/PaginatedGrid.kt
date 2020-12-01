package tz.co.asoft

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.html.DIV
import react.*
import styled.StyledDOMBuilder
import tz.co.asoft.PaginatedGrid.Props
import tz.co.asoft.PaginatedGrid.State

@JsExport
class PaginatedGrid<D : Any> private constructor(p: Props<D>) : RComponent<Props<D>, State<D>>(p), CoroutineScope by CoroutineScope(SupervisorJob()) {
    class Props<D : Any>(
        val pager: Pager<*, D>,
        val cols: String,
        val rows: String,
        val gap: String,
        val builder: StyledDOMBuilder<DIV>.(D) -> Unit
    ) : RProps

    class State<D : Any>(var value: PagingState<*, D>) : RState

    init {
        state = State(props.pager.state.value)
    }

    private var observer: Job? = null
    override fun componentWillMount() {
        observer = props.pager.observe()
    }

    fun Pager<*, D>.observe() = launch {
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
        is PagingState.Showing -> Grid(rows = "1fr auto") {
            GridAdapter(
                ui.page.data,
                cols = props.cols,
                gap = props.gap,
                rows = props.rows,
                builder = props.builder
            )
            Paginator(
                onPrev = { props.pager.loadPrevious() }.takeIf { ui.page.prev != null },
                onNext = { props.pager.loadNext() }.takeIf { ui.page.nextKey != null }
            )
        }
        is PagingState.Error -> Error(ui.cause?.message ?: "Unknown error")
    }
}

fun <D : Any> RBuilder.PaginatedGrid(
    pager: Pager<*, D>,
    cols: String = "1fr",
    rows: String = "1fr",
    gap: String = "1em",
    builder: StyledDOMBuilder<DIV>.(D) -> Unit
) = child(PaginatedGrid::class.js, Props(pager, cols, rows, gap, builder)) {}