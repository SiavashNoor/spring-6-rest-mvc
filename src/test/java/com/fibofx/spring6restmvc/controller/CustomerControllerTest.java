package com.fibofx.spring6restmvc.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fibofx.spring6restmvc.model.CustomerDTO;
import com.fibofx.spring6restmvc.services.CustomerService;
import com.fibofx.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper;

    CustomerServiceImpl  customerServiceImpl;


    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

    @BeforeEach
    void setUp(){
        customerServiceImpl = new CustomerServiceImpl();
    }


    @Test
    void listAllCustomersTest() throws Exception {

        given(customerService.getAllCustomers()).willReturn(customerServiceImpl.getAllCustomers());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()",is(3)));

    }

    @Test
    void getCustomerByIdTest() throws Exception {
        CustomerDTO testCustomer = customerServiceImpl.getAllCustomers().getFirst();

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH+"/"+testCustomer.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(testCustomer.getId().toString())))
                .andExpect(jsonPath("$.name",is(testCustomer.getName())));

    }


    //test validation for customer dto
    @Test
    void testCreateNewCustomerWithNullData() throws Exception {


        CustomerDTO customerDTO = CustomerDTO.builder()
                .build();

        given(customerService.saveNewCustomer(any(CustomerDTO.class))).willReturn(customerServiceImpl.getAllCustomers().get(1));
       MvcResult r =  mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest())
               .andReturn();
        System.out.println(r.getResponse().getContentAsString());
    }

    @Test
    void testCreateNewCustomer() throws Exception {

        List<CustomerDTO> customers = customerServiceImpl.getAllCustomers();
        CustomerDTO firstCustomer = customers.getFirst();

        System.out.println("Before modification:");
        System.out.println("firstCustomer name: " + firstCustomer.getName());
        System.out.println("customers.get(0) name: " + customers.get(0).getName());

// Modify through the reference
        firstCustomer.setName("Siavash");

        System.out.println("After modification:");
        System.out.println("firstCustomer name: " + firstCustomer.getName());
        System.out.println("customers.get(0) name: " + customers.get(0).getName());

      CustomerDTO savedCustomer= customerServiceImpl.getAllCustomers().getFirst();
        savedCustomer.setId(null);
        savedCustomer.setName("Siavash");
        savedCustomer.setVersion(2);

        given(customerService.saveNewCustomer(any(CustomerDTO.class))).willReturn(customerServiceImpl.getAllCustomers().get(2));

        mockMvc.perform(post(CustomerController.CUSTOMER_PATH).accept(MediaType.APPLICATION_JSON)

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedCustomer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }


    @Test
    void deleteByIdTest() throws Exception {
        CustomerDTO customer = customerServiceImpl.getAllCustomers().getFirst();

        given(customerService.deleteById(any())).willReturn(true);
        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH+"/"+customer.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(customerService).deleteById(uuidArgumentCaptor.capture());
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void getCustomerByIdNotFound() throws Exception {

       given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());
       mockMvc.perform(get(CustomerController.CUSTOMER_PATH+UUID.randomUUID())
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
        }

        @Test
        void testUpdateNewCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.getAllCustomers().getFirst();
         given(customerService.updateCustomerById(any(),any())).willReturn(Optional.of(customer));
        mockMvc.perform(put(CustomerController.CUSTOMER_PATH+"/"+customer.getId().toString()).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());


        //This verifies that the mocked customerService was called exactly once with the expected parameters.
            //uuidArgumentCaptor and customerArgumentCaptor are Mockito ArgumentCaptors, which capture the arguments passed to the method for later inspection.
        verify(customerService).updateCustomerById(uuidArgumentCaptor.capture(),customerArgumentCaptor.capture());
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customer.getName()).isEqualTo(customerArgumentCaptor.getValue().getName());
        }


@Test
    void testPatchBeer() throws Exception{

        CustomerDTO customer = customerServiceImpl.getAllCustomers().getFirst();

        Map<String,Object> customerMap = new HashMap<>();
        customerMap.put("name","newNameIsSet");
        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH+"/"+customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());
        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(),customerArgumentCaptor.capture());
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customerMap.get("name")).isEqualTo(customerArgumentCaptor.getValue().getName());
}








}
