package com.fibofx.spring6restmvc.repositories;

import com.fibofx.spring6restmvc.entities.Customer;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
    Customer savedCustomer = customerRepository.save(Customer.builder()
                    .name("Siavash N")
            .build());
    customerRepository.flush();
    assertThat(savedCustomer).isNotNull();
    assertThat(savedCustomer.getId()).isNotNull();
    }


    @Test
    void TestCustomerNameTooLong() {
        assertThrows(ConstraintViolationException.class,()->{
            Customer savedCustomer = customerRepository.save(Customer.builder()
                    .name("Siavash N12345678N12345678N12345678N12345678N12345678N12345678N12345678N12345678N12345678N12345678N12345678N12345678")
                    .build());
            customerRepository.flush();

        });

    }
}