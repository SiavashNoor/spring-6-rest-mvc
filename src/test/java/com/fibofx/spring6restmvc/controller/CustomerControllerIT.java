package com.fibofx.spring6restmvc.controller;

import com.fibofx.spring6restmvc.entities.Customer;
import com.fibofx.spring6restmvc.mappers.CustomerMapper;
import com.fibofx.spring6restmvc.model.CustomerDTO;
import com.fibofx.spring6restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerMapper customerMapper;



    @Test
    void testDeleteByIdCustomerNotFound() {
        assertThrows(NotFoundException.class,()->{
            customerController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteByIdForCustomer() {
        Customer customer = customerRepository.findAll().getFirst();
        ResponseEntity responseEntity = customerController.deleteById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void testUpdateCustomerWhenNotFound() {
        assertThrows(NotFoundException.class,()->{
            customerController.updateById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateExistingCustomer() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String customerName = "updated Customer Name ,james";
        customerDTO.setName(customerName);

        ResponseEntity responseEntity = customerController.updateById(customer.getId(),customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(customerName);

    }


    @Rollback
    @Transactional
    @Test
    void testSaveNewBeer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("new customer")
                .build();
        ResponseEntity responseEntity = customerController.handlePost(customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        Customer customer = customerRepository.findById(savedUUID).get();
        assertThat(customer).isNotNull();

    }


    @Test
    void testGetByIdNotFound() {
        assertThrows(NotFoundException.class,()->{
            customerController.getCustomerById(UUID.randomUUID());}
        );

    }

    @Test
    void testGetById() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO dto = customerController.getCustomerById(customer.getId());
        assertThat(dto).isNotNull();
    }

    @Test
    void testListCustomers() {

        List<CustomerDTO> dtos = customerController.listAllCustomers();
        assertThat(dtos.size()).isEqualTo(3);
    }


    @Rollback
    @Transactional
    @Test
    void testListOfCustomersIfWasEmpty() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.listAllCustomers();
        assertThat(dtos.size()).isEqualTo(0);

    }
}