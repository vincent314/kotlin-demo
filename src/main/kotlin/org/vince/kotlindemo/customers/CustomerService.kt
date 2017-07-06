package org.vince.kotlindemo.customers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.InputStream
import java.math.BigDecimal
import java.math.RoundingMode

fun readCustomers(inputStream: InputStream): List<Customer?> =
        jacksonObjectMapper()
                .readValue<Array<Customer?>>(inputStream)
                .asList()

fun groupTurnoverByCountry(customers: List<Customer>): TurnoverByCountry =
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
