package com.fibofx.spring6restmvc.services;

import com.fibofx.spring6restmvc.model.CustomerDTO;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.*;



public class CustomerServiceImpl implements CustomerService {
    private Map<UUID, CustomerDTO> customerMap;
    public  CustomerServiceImpl(){
        CustomerDTO customer1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 2")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 3")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap = new HashMap<>();
        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);

    }
    @Override
    public List<CustomerDTO> getAllCustomers() {
        return new ArrayList<>(customerMap.values());
    }
    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.of(customerMap.get(id));
    }

    @Override
    public boolean deleteById(UUID customerId) {
        customerMap.remove(customerId);
        return true;
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        CustomerDTO savedCustomer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name(customer.getName())
                .version(customer.getVersion())
                .updateDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .build();
        customerMap.put(savedCustomer.getId(),savedCustomer);
        return savedCustomer;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {

        CustomerDTO existing  = customerMap.get(customerId);
        existing.setName(customer.getName());
        existing.setVersion(customer.getVersion());

        customerMap.put(existing.getId(),existing);
        return Optional.of(existing);
    }


    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID id, CustomerDTO customer) {
        CustomerDTO existing  = customerMap.get(id);

        if(StringUtils.hasText(customer.getName())){
            existing.setName(customer.getName());
        }

        return Optional.of(existing);
    }
}
