package org.vince.kotlindemo.step2

import org.amshove.kluent.shouldBeEqualTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object Step2Spec:Spek ({
    class User {
        var firstname:String = ""
            get() = field.capitalize()
        var surname:String = ""
            set(value) {
                field = value.toUpperCase()
            }
    }

    describe("Getter & Setters"){
        it("Should set Uppercase value"){
            val user = User()
            user.firstname = "john"
            user.surname = "doe"

            "${user.firstname} ${user.surname}".shouldBeEqualTo("John DOE")
        }
    }
})