package tz.co.asoft

import kotlinx.browser.document
import react.dom.render

fun main() {
    render(document.getElementById("root")) {
        Grid {
            +"Works"
        }
    }
}