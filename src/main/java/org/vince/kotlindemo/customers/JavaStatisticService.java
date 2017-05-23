package org.vince.kotlindemo.customers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaStatisticService {

    public List<JavaCustomer> readCustomers(InputStream inputStream) throws IOException {
        return new ObjectMapper().readValue(
                inputStream,
                JavaCustomerContainer.class
        ).getData();
    }

    public Map<String, BigDecimal> groupTurnoverByCountry(InputStream inputStream) throws IOException {
        Map<String, List<JavaCustomer>> byCountry = readCustomers(inputStream).stream()
                .collect(Collectors.groupingBy(JavaCustomer::getCountry));

        return byCountry.entrySet().stream()
                .map(entry -> {
                    List<JavaCustomer> customers = entry.getValue();

                    Double sum = customers.stream()
                            .map(JavaCustomer::getTurnover)
                            .mapToDouble(str -> Double
                                    .valueOf(str.substring(1).replace(',', '.')))
                            .sum();

                    return new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), new BigDecimal(sum).setScale(2, BigDecimal.ROUND_HALF_UP));
                })
                .collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue));
    }
}
