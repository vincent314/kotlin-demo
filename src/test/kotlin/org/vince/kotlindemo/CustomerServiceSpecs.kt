package org.vince.kotlindemo

import org.amshove.kluent.`should equal`
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.springframework.core.io.ClassPathResource
import org.vince.kotlindemo.customers.Customer
import org.vince.kotlindemo.customers.CustomerService
import java.math.BigDecimal
import java.math.RoundingMode

object CustomerServiceSpecs : Spek({
    fun Double.toBigDecimal() = BigDecimal(this).setScale(2, RoundingMode.HALF_UP)

    describe("Statistic Service") {

        given("2 customers") {
            it("should group by country with Kotlin") {
                val customers: List<Customer> = listOfNotNull(
                        null, // CAUTION !
                        Customer(
                                id = "1234",
                                firstname = "John",
                                lastname = "Doe",
                                email = "john.doe@email.com",
                                turnover = "€10,0",
                                country = "France"
                        ),
                        Customer(
                                id = "4567",
                                firstname = "Dr",
                                lastname = "Who",
                                email = "dr.who@gallifrey.com",
                                turnover = "€20,0",
                                country = "France"
                        )
                )

                CustomerService.groupTurnoverByCountry(customers) shouldEqual hashMapOf("France" to 30.0.toBigDecimal())
            }
        }

        given("a test JSON file") {
            val testResource = ClassPathResource("customers.json")
            val expectedValues = hashMapOf(
                    "Belgium" to 213_288.28.toBigDecimal(),
                    "Luxembourg" to 527_724.3.toBigDecimal(),
                    "Andorra" to 46_901.98.toBigDecimal(),
                    "United Kingdom" to 640_429.14.toBigDecimal(),
                    "France" to 11_479_296.52.toBigDecimal(),
                    "Switzerland" to 218_024.96.toBigDecimal(),
                    "Germany" to 1_207_424.78.toBigDecimal(),
                    "Spain" to 1_041_106.04.toBigDecimal()
            )

            it("should group by country with Kotlin") {
                val customers = CustomerService
                        .readCustomers(testResource.inputStream)
                        .filterNotNull()
                CustomerService.groupTurnoverByCountry(customers) `should equal` expectedValues
            }
        }

        given("a JSON test file with null values") {
            val testResource = ClassPathResource("customers-with-null.json")

            it("should not be able to read file") {
                val customers: List<Customer?> = CustomerService
                        .readCustomers(testResource.inputStream)
                customers.size shouldBe 5
            }
        }
    }
})