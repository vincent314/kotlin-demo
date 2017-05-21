package org.vince.kotlindemo.customers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

public class StatisticService {

    private class CustomerContainer{
        private List<Customer> data;

        public List<Customer> getData() {
            return data;
        }

        public void setData(List<Customer> data) {
            this.data = data;
        }
    }

    public List<Customer> getCustomers() throws IOException {
        return new ObjectMapper().readValue(
                new ClassPathResource("customers.json").getInputStream(),
                CustomerContainer.class
        ).getData();
    }

}
