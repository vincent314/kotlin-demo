package org.vince.kotlindemo.dataclass

import org.amshove.kluent.shouldEqualTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object Step3Spec:Spek ({
    data class User(var firstname:String, var surname:String)

    describe("Data class"){
        it("Should use destructured declaration"){
            val (firstname, surname) = User(firstname = "John", surname = "DOE")
            "$firstname $surname" shouldEqualTo "John DOE"
        }
    }
})