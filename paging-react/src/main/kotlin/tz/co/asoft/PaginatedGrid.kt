package tz.co.asoft

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.html.DIV
import react.*
import styled.StyledDOMBuilder
import tz.co.asoft.PaginatedGrid.Props
import tz.co.asoft.PaginatedGrid.State

@JsExport
class PaginatedGrid<D> private constructor(p: Props<D>) : Component<Props<D>, State<D>>(p) {
    class Props<D>(
        val pager: Pager<D>,
        val cols: String,
        val gap: String,
        val header: (StyledDOMBuilder<DIV>.() -> Unit)?,
        val builder: StyledDOMBuilder<DIV>.(D?) -> Unit
    ) : RProps

    class State<D>(var pager: Pager.State<D>) : RState

    init {
        state = State(props.pager.state.value)
    }

    private var observer: Job? = null
    override fun componentWillMount() {
        observer = props.pager.observe()
    }

    private fun Pager<D>.observe() = launch {
        state.collect {
            setState { pager = it }
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

    private fun RBuilder.ShowGrid(page: Page<D>?) = Grid(rows = "30px 1fr", gap = "0.5em") {
        Paginator(
            onPrev = { props.pager.loadPrevious() }.takeIf { props.pager.canLoadPrevious() },
            center = props.header,
            onNext = { props.pager.loadNext() }.takeIf { props.pager.canLoadNext() }
        )
        GridAdapter(
            data = page?.data ?: List(props.pager.pageSize) { null },
            cols = props.cols,
            gap = props.gap,
            rows = "1fr",
            builder = props.builder
        )
    }

    override fun RBuilder.render(): dynamic = when (val ui = state.pager) {
        is Pager.State.Loading -> ShowGrid(ui.cachedPage)
        is Pager.State.Showing -> ShowGrid(ui.page)
        is Pager.State.Error -> Error(ui.cause?.message ?: "Unknown error")
    }
}

fun <D> RBuilder.PaginatedGrid(
    fetcher: PageFetcher<D>,
    cols: String,
    gap: String = "1em",
    header: (StyledDOMBuilder<DIV>.() -> Unit)? = null,
    builder: StyledDOMBuilder<DIV>.(D?) -> Unit
) = child(PaginatedGrid::class.js, Props(fetcher.pager, cols, gap, header, builder)) {}

fun <D> RBuilder.PaginatedGrid(
    pager: Pager<D>,
    cols: String,
    gap: String = "1em",
    header: (StyledDOMBuilder<DIV>.() -> Unit)? = null,
    builder: StyledDOMBuilder<DIV>.(D?) -> Unit
) = child(PaginatedGrid::class.js, Props(pager, cols, gap, header, builder)) {}

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
