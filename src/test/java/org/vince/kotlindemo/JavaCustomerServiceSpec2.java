package org.vince.kotlindemo;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.vince.kotlindemo.customers.JavaCustomer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.junit.Assert.assertEquals;
import static org.vince.kotlindemo.customers.JavaCustomerService.groupTurnoverByCountry;
import static org.vince.kotlindemo.customers.JavaCustomerService.readCustomers;

public class JavaCustomerServiceSpec2 {

    private ClassPathResource resource;
    private Map<String, BigDecimal> expectedValues;

    @Before
    public void setup() {
        resource = new ClassPathResource("customers.json");

        expectedValues = new HashMap<>();
        expectedValues.put("Belgium", new BigDecimal(213_288.28).setScale(2, ROUND_HALF_UP));
        expectedValues.put("Luxembourg", new BigDecimal(527_724.3).setScale(2, ROUND_HALF_UP));
        expectedValues.put("Andorra", new BigDecimal(46_901.98).setScale(2, ROUND_HALF_UP));
        expectedValues.put("United Kingdom", new BigDecimal(640_429.14).setScale(2, ROUND_HALF_UP));
        expectedValues.put("France", new BigDecimal(11_479_296.52).setScale(2, ROUND_HALF_UP));
        expectedValues.put("Switzerland", new BigDecimal(218_024.96).setScale(2, ROUND_HALF_UP));
        expectedValues.put("Germany", new BigDecimal(1_207_424.78).setScale(2, ROUND_HALF_UP));
        expectedValues.put("Spain", new BigDecimal(1_041_106.04).setScale(2, ROUND_HALF_UP));
    }

    @Test
    public void shouldGroupByCountryWithJava() throws IOException {
        List<JavaCustomer> customers = readCustomers(resource.getInputStream());
        assertEquals(expectedValues, groupTurnoverByCountry(customers));
    }
}
