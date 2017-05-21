package org.vince.kotlindemo.customers

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ClassPathResource

class StatisticService {

    private data class CustomerContainer(var data: List<Customer>)

    fun customers(): List<Customer> =
            ObjectMapper().readValue(
                    ClassPathResource("customers.json").inputStream,
                    CustomerContainer::class.java
            ).data

}
