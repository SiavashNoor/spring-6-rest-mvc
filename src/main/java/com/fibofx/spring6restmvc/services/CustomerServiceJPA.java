package com.fibofx.spring6restmvc.services;

import com.fibofx.spring6restmvc.entities.Customer;
import com.fibofx.spring6restmvc.mappers.CustomerMapper;
import com.fibofx.spring6restmvc.model.CustomerDTO;
import com.fibofx.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private  final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper.customerToCustomerDto(customerRepository.findById(id).orElse(null)));
    }

    @Override
    public boolean deleteById(UUID customerId) {
            if(customerRepository.existsById(customerId)){
                customerRepository.deleteById(customerId);
                return true;
            }
            return false;
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {

        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
        return customerRepository.findById(customerId)
                .map(existingCustomer ->updateAndSave(existingCustomer,customer))
                .map(customerMapper::customerToCustomerDto);
    }

    private Customer updateAndSave(Customer existingCustomer, CustomerDTO customer) {
    existingCustomer.setName(customer.getName());
    return customerRepository.save(existingCustomer);
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID id, CustomerDTO customer) {

        return Optional.empty();
    }
}
