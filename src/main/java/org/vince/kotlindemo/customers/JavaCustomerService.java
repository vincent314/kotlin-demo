package org.vince.kotlindemo.customers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class JavaCustomerService {
    public static List<JavaCustomer> readCustomers(InputStream inputStream) throws IOException {
        return Arrays.asList(new ObjectMapper().readValue(
                inputStream,
                JavaCustomer[].class
        ));
    }

    public static Map<String, BigDecimal> groupTurnoverByCountry(List<JavaCustomer> customers){
        ToDoubleFunction<String> parseCurrency = str ->
                Double.valueOf(str.substring(1).replace(',', '.'));

        Map<String, List<JavaCustomer>> byCountry = customers.stream()
                .collect(Collectors.groupingBy(JavaCustomer::getCountry));

        return byCountry.entrySet().stream()
                .map(entry -> {
                    List<JavaCustomer> subList = entry.getValue();

                    Double sum = subList.stream()
                            .map(JavaCustomer::getTurnover)
                            .filter(Objects::nonNull)
                            .mapToDouble(parseCurrency)
                            .sum();

                    return new AbstractMap.SimpleImmutableEntry<>(
                            entry.getKey(),
                            new BigDecimal(sum).setScale(2, BigDecimal.ROUND_HALF_UP));
                })
                .collect(Collectors.toMap(
                        AbstractMap.SimpleImmutableEntry::getKey,
                        AbstractMap.SimpleImmutableEntry::getValue)
                );
    }
}
