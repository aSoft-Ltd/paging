package tz.co.asoft

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.css.*
import kotlinx.css.Color
import kotlinx.css.properties.boxShadow
import react.RBuilder
import react.RElementBuilder
import react.RProviderProps
import react.dom.div
import react.router.dom.browserRouter
import styled.css


fun main() = document.getElementById("root").setContent {
    val data = Person.randomPeople(300).sortedBy { it.name }
    val source = peopleSource(data)
    val gridFetcher = PageFetcher(source, 40)
    val listFetcher = PageFetcher(source, 6)
    ThemeProvider(AquaGreenTheme()) {
        browserRouter { SandBox(gridFetcher, listFetcher) }
    }
}

private fun RBuilder.SandBox(
    gridFetcher: PageFetcher<Person>,
    listFetcher: PageFetcher<Person>
) = ThemeConsumer {
    Surface {
        css {
            margin(1.em)
            width = 100.pct - 2.em
            boxShadow(Color.gray, blurRadius = 4.px, spreadRadius = 2.px)
        }
        PaginatedGrid(gridFetcher.pager, gap = "0.5em", cols = "1fr 1fr 1fr 1fr 1fr") { person ->
            css { children { alignSelf = Align.center } }
            PersonView(person)
        }
    }

    Surface {
        css {
            margin(1.em)
            width = 100.pct - 2.em
            boxShadow(Color.gray, blurRadius = 4.px, spreadRadius = 2.px)
            boxShadow(Color.gray, blurRadius = 4.px, spreadRadius = 2.px)
        }
        PaginatedList(listFetcher, gap = "0.5em") { person ->
            css { children { alignSelf = Align.center } }
            PersonView(person)
        }
    }

    Surface {
        css {
            margin(1.em)
            width = 100.pct - 2.em
            boxShadow(Color.gray, blurRadius = 4.px, spreadRadius = 2.px)
            boxShadow(Color.gray, blurRadius = 4.px, spreadRadius = 2.px)
        }

        PaginatedTable(
            listFetcher,
            columns = listOf(
                Column("Name") { it?.name ?: "firstname lastname" },
                Column("Age") { it?.age?.toString() ?: "N/A" }
            ),
            actions = listOf(
                AButton.Contained("Submit", FaCheck) { window.alert(it?.name ?: "Unknown") },
                AButton.Outlined("Cancel", FaTimes) { window.alert(it?.age?.toString() ?: "Unkown age") }
            )
        )
    }
}