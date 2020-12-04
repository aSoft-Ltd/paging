package tz.co.asoft

data class Person(val name: String, val age: Int) {
    companion object {
        val names = listOf(
            "Raiden", "Jax", "Sonya", "Johny", "Cage", "Briggs",
            "Hanzo", "Hashashi", "Liu", "Lao", "Kun", "Jin", "Kang", "Kung",
            "Cano", "Shang", "Tsung", "Shao", "Kotal", "Tanya", "Kitana", "Ermac",
            "Baraka", "Kabal", "Cetrion", "Geras", "Takeda", "Keshi", "Takahashi"
        )

        fun random() = Person(name = names.random() + " " + names.random(), age = (24..1000).random())

        fun randomPeople(n: Int = 100) = List(n) { random() }
    }
}