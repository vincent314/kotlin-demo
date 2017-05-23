package org.vince.kotlindemo

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.springframework.core.io.ClassPathResource
import org.vince.kotlindemo.customers.JavaStatisticService
import org.vince.kotlindemo.customers.StatisticService
import java.math.BigDecimal
import java.math.RoundingMode

suspend fun doTimer(title: String, delay:Long=1000L) {
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
                        launch(CommonPool) {
                            doTimer("job A")
                        },
                        launch(CommonPool) {
                            doTimer("job B", 500L)
                        })
                println("end")
                jobs.forEach { it.join() }
            }
        }
    }

    describe("Objects"){
        given("an object") {
            val o = object {
                var amount = 40
                var currency = "€"
            }

            it("amount should be formatted") {
                "${o.amount} ${o.currency}" shouldEqualTo "40 €"
            }
        }
    }

    fun Double.toBigDecimal() = BigDecimal(this).setScale(2, RoundingMode.HALF_UP)

    describe("Statistic Service"){
        given("a test JSON file"){
            val testResource = ClassPathResource("customers.json")
            val expectedValues = hashMapOf(
                    "Belgium" to 213_288.28.toBigDecimal(),
                    "Luxembourg" to 527_724.3.toBigDecimal(),
                    "Andorra" to 46_901.98.toBigDecimal(),
                    "United Kingdom" to 640_429.14.toBigDecimal(),
                    "France" to 11_488_616.63.toBigDecimal(),
                    "Switzerland" to 218_024.96.toBigDecimal(),
                    "Germany" to 1_207_424.78.toBigDecimal(),
                    "Spain" to 1_041_106.04.toBigDecimal()
            )

            it("should group by country with Java"){
                JavaStatisticService().groupTurnoverByCountry(testResource.inputStream) shouldEqual expectedValues
            }

            it("should group by country with Kotlin"){
                StatisticService().groupTurnoverByCountry(testResource.inputStream) shouldEqual expectedValues
            }
        }

    }
})