package com.fibofx.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fibofx.spring6restmvc.entities.Customer;
import com.fibofx.spring6restmvc.mappers.CustomerMapper;
import com.fibofx.spring6restmvc.model.CustomerDTO;
import com.fibofx.spring6restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvc.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerMapper customerMapper;


    //“Inject the entire Spring Web Application context.”
    @Autowired
    WebApplicationContext wac;


    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        /**
         * means:
         *
         * “Build a MockMvc object using the full Spring WebApplicationContext.”
         *
         * So instead of the automatically autowired mockMvc, you manually build a new one that uses the entire application context.
         *
         * This setup is useful when:
         *
         * ✔ You want full controller testing (controller + filters + interceptors)
         * ✔ You need all beans loaded
         * ✔ You want to test the application almost like it's running normally
         */

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    @Test
    void testPatchCustomerNameTooLong() throws Exception {

        String longName = "new long name 12345456788991234545678899123454567889912345456788991234545678899123454567889912345456788991234545678899";

        Customer customer = customerRepository.findAll().getFirst();

        Map<String ,String > customerMap = new HashMap<>();
        customerMap.put("name",longName);

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH+"/"+customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isBadRequest());



    }

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