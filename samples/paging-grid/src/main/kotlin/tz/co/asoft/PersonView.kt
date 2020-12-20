package tz.co.asoft

import kotlinext.js.jsObject
import kotlinx.css.borderTop
import kotlinx.css.em
import kotlinx.css.paddingTop
import react.RBuilder
import react.RProps
import react.dom.div
import react.functionalComponent
import react.router.dom.withRouter
import styled.css

fun RBuilder.PersonView(
    person: Person?
) = child(withRouter(PersonViewHook), jsObject<PersonProps>().also {
    it.person = person
}) {}

private external interface PersonProps : RProps {
    var person: Person?
}

private val PersonViewHook = functionalComponent<PersonProps> { props ->
    val person = props.person
    Grid(cols = "50px 1fr", gap = 0.5.em) { theme ->
        css {
            borderTop = "solid 1px ${theme.onPrimaryColor}"
            paddingTop = 0.5.em
        }
        ProfilePic(person?.name ?: "Not Applicable")
        Grid(rows = "1fr 1fr", gap = 0.5.em) {
            div { +(person?.name ?: "Firstname Lastname") }
            div { +"${person?.age ?: "..."} years old" }
            TextButton("View", FaEye) { props.history.push("/d") }
        }
    }
}