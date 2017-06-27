package org.vince.kotlindemo

import org.amshove.kluent.shouldEqualTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

object JapaneseSpec : Spek({

    given("One person Yamamoto") {
        val この人 = 人(名前 = "山本")

        it("This person's name should be 'Yamamoto'") {
            この人 の 名前 は "山本" です か
            // thisPerson.name shouldEquals "Yamamoto"
        }
    }
})

val か = null

infix fun Pair<String,String>.です(other: Nothing?) = this.first shouldEqualTo this.second

infix fun String.は(other: String) = Pair(this, other)

val 名前 = 人::名前

data class 人(var 名前: String) {
    infix fun の(f: (人) -> String) = f.invoke(this)
}