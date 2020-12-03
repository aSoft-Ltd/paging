package tz.co.asoft

import kotlinx.browser.document
import kotlinx.css.*
import kotlinx.css.Color
import kotlinx.css.properties.boxShadow
import react.RBuilder
import react.dom.div
import styled.css


fun main() = document.getElementById("root").setContent {
    val data = Person.randomPeople(300).sortedBy { it.name }
    val source = peopleSource(data)
    val gridFetcher = PageFetcher(source, 40)
    val listFetcher = PageFetcher(source, 6)
    ThemeProvider(AquaGreenTheme()) {
        ThemeConsumer {
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
                PaginatedList(listFetcher.pager, gap = "0.5em") { person ->
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
                    listFetcher.pager,
                    columns = listOf(
                        Column("Name") { it.name },
                        Column("Age") { it.age.toString() }
                    ),
                    actions = listOf(
                        AButton.Contained("Submit", FaCheck) {},
                        AButton.Outlined("Cancel", FaCross) {}
                    )
                )
            }
        }
    }
}