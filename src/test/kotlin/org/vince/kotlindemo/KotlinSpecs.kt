package org.vince.kotlindemo

import kotlinx.coroutines.experimental.*
import org.amshove.kluent.shouldBeEqualTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

suspend fun doTimer(title: String, delay: Long = 1000L) {
    for (i in 1..4) {
        println("$title - Iteration $i")
        delay(delay)
    }
}

object KotlinSpecs : Spek({
    describe("Val vs var") {
        it("should assign to var") {
            val value = 1
            var variable = 1

            // forbidden : value = 2
            variable = 2
        }
    }

    describe("Classes") {
        class Car(b: String) {
            private var started = false
            var brand: String = ""

            init {
                brand = b
            }

            fun start() {
                started = true
                println("$brand car started")
            }
        }

        class OptimizedCar(b: String) {
            private var started = false
            var brand: String = b
            fun start() {
                started = true
                println("$brand car started")
            }
        }

        it("should start BMW car") {
            Car("BMW").start()
        }

        it("should start optimized BMW car") {
            OptimizedCar("Tesla").start()
        }
    }

    describe("Coroutines") {
        it("should run 2 timers") {
            runBlocking {
                println("start !")
                val jobs = arrayOf(
                        launch(Dispatchers.Default) {
                            doTimer("job A")
                        },
                        launch(Dispatchers.Default) {
                            doTimer("job B", 500L)
                        })
                println("end")
                jobs.forEach { it.join() }
            }
        }
    }

    describe("Objects") {
        given("an object") {
            val o = object {
                var amount = 40
                var currency = "€"
            }

            it("amount should be formatted") {
                "${o.amount} ${o.currency}" shouldBeEqualTo "40 €"
            }
        }
    }
})