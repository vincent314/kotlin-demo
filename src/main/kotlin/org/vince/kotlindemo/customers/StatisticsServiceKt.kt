package org.vince.kotlindemo.customers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.IOException
import java.io.InputStream
import java.math.BigDecimal
import java.math.RoundingMode

class StatisticService {

    fun String.readCurrency(): Double = this.substring(1).replace(',', '.').toDouble()

    fun readCustomers(inputStream:InputStream): List<Customer> =
            ObjectMapper()
                    .registerModule(KotlinModule())
                    .readValue(
                            inputStream,
                            CustomerContainer::class.java
                    ).data

    @Throws(IOException::class)
    fun groupTurnoverByCountry(inputStream:InputStream): Map<String, BigDecimal> =
            readCustomers(inputStream).groupBy(Customer::country)
                    .mapValues { (_, customers) ->
                        BigDecimal(
                                customers.map(Customer::turnover)
                                        .map { it.readCurrency() }
                                        .sum()
                        ).setScale(2, RoundingMode.HALF_UP)

                    }
}
