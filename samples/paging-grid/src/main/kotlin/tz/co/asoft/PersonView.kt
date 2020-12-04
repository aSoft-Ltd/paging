package tz.co.asoft

import kotlinx.css.borderTop
import kotlinx.css.em
import kotlinx.css.paddingTop
import react.RBuilder
import react.dom.div
import styled.css

fun RBuilder.PersonView(person: Person?) = Grid(cols = "50px 1fr", gap = "0.5em") { theme ->
    css {
        borderTop = "solid 1px ${theme.onPrimaryColor}"
        paddingTop = 0.5.em
    }
    ProfilePic(person?.name ?: "Not Applicable")
    Grid(rows = "1fr 1fr", gap = "0.5em") {
        div { +(person?.name ?: "Firstname Lastname") }
        div { +"${person?.age ?: "..."} years old" }
    }
}