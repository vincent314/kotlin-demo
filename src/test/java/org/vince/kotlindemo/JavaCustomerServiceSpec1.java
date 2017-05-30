package org.vince.kotlindemo;

import org.junit.Before;
import org.junit.Test;
import org.vince.kotlindemo.customers.JavaCustomer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.junit.Assert.assertEquals;
import static org.vince.kotlindemo.customers.JavaCustomerService.groupTurnoverByCountry;

public class JavaCustomerServiceSpec1 {

    private List<JavaCustomer> customers;

    @Before
    public void setup(){
        customers = Arrays.asList(
                null,
                new JavaCustomer(
                        "1234",
                        "John",
                        "Doe",
                        "john.doe@email.com",
                        "€10,0",
                        "France"
                ),
                new JavaCustomer(
                        "4567",
                        "Dr",
                        "Who",
                        "dr.who@gallifrey.com",
                        "€20,0",
                        "France"
                )
        );

    }

    @Test
    public void shouldGroupByCountry() throws IOException {
        Map<String, BigDecimal> result = groupTurnoverByCountry(
                customers.stream().filter(Objects::nonNull).collect(Collectors.toList())
        );
        assertEquals(new BigDecimal(30.0).setScale(2, ROUND_HALF_UP),result.get("France"));
    }
}
