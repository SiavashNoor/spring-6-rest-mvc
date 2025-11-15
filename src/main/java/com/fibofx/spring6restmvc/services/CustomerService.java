package com.fibofx.spring6restmvc.services;

import com.fibofx.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {



    public List<CustomerDTO> getAllCustomers();

    public Optional<CustomerDTO> getCustomerById(UUID id);


    public void deleteById(UUID beerId);

    public CustomerDTO saveNewCustomer(CustomerDTO customer);

    public void updateCustomerById(UUID customerId, CustomerDTO customer);

    public void patchCustomerById(UUID id, CustomerDTO customer);
}
