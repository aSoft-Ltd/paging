package tz.co.asoft

import react.RBuilder
import styled.styledDiv

fun RBuilder.Paginator(
    onPrev: (() -> Unit)? = null,
    onNext: (() -> Unit)? = null
) = Grid(cols = "auto auto auto") {
    if (onPrev != null) {
        ContainedButton("Prev", FaArrowLeft, onPrev)
    } else {
        styledDiv { }
    }

    styledDiv { }

    if (onNext != null) {
        ContainedButton("Next", FaArrowRight, onNext)
    } else {
        styledDiv { }
    }
}