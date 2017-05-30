package org.vince.kotlindemo.customers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.InputStream
import java.math.BigDecimal
import java.math.RoundingMode

object CustomerService {
    fun readCustomers(inputStream: InputStream): List<Customer?> =
            ObjectMapper()
                    .registerModule(KotlinModule())
                    .readValue(inputStream, Array<Customer?>::class.java)
                    .asList()

    fun groupTurnoverByCountry(customers:List<Customer>): TurnoverByCountry =
            customers
                    .groupBy(Customer::country)
                    .mapValues { (_, customers) ->
                        BigDecimal(
                                customers.map(Customer::turnover)
                                        .filterNotNull()
                                        .map { it.parseCurrency() }
                                        .sum()
                        ).setScale(2, RoundingMode.HALF_UP)
                    }
}
