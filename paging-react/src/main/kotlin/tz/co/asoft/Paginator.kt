package tz.co.asoft

import kotlinx.css.*
import kotlinx.css.properties.border
import kotlinx.css.properties.s
import kotlinx.css.properties.transition
import kotlinx.html.DIV
import kotlinx.html.js.onClickFunction
import react.RBuilder
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv

fun RBuilder.Paginator(
    onPrev: (() -> Unit)? = null,
    center: (StyledDOMBuilder<DIV>.() -> Unit)? = null,
    onNext: (() -> Unit)? = null
) = Grid(cols = "auto 1fr auto") { theme ->
    css {
        children { alignSelf = Align.center }
    }
    val styles: CSSBuilder.() -> Unit = {
        border(1.px, BorderStyle.solid, theme.onSurfaceColor.withAlpha(0.0))
        cursor = Cursor.pointer
        padding(0.5.rem)
        transition(duration = 0.2.s)
    }

    styledDiv {
        css {
            color = theme.onSurfaceColor.withAlpha(if (onPrev == null) 0.5 else 1.0)
            +styles
            if (onPrev != null) hover {
                border(1.px, BorderStyle.solid, theme.onSurfaceColor.withAlpha(1.0))
            }
        }
        onPrev?.let { attrs.onClickFunction = { it() } }
        FaArrowLeft {}
    }

    styledDiv { center?.let { it() } }

    styledDiv {
        css {
            css { color = theme.onSurfaceColor.withAlpha(if (onNext == null) 0.5 else 1.0) }
            +styles
            if (onNext != null) hover {
                border(1.px, BorderStyle.solid, theme.onSurfaceColor.withAlpha(1.0))
            }
        }
        onNext?.let { attrs.onClickFunction = { it() } }
        FaArrowRight {}
    }
}