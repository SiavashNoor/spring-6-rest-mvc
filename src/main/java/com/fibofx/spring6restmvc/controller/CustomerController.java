package com.fibofx.spring6restmvc.controller;

import com.fibofx.spring6restmvc.model.CustomerDTO;
import com.fibofx.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class CustomerController {
    public static  final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH+"/{customerId}";

    private final CustomerService customerService;


    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> listAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID id){
        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }


    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("customerId") UUID customerId){
        customerService.deleteById(customerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity<CustomerDTO> handlePost(@RequestBody CustomerDTO customer){
        CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",CUSTOMER_PATH+"/"+savedCustomer.getId().toString());
        return new ResponseEntity<>(headers,HttpStatus.CREATED);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> updateById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer){
        customerService.updateCustomerById(customerId,customer);
       return new ResponseEntity<>(HttpStatus.NO_CONTENT) ;
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerPatchById(@PathVariable("customerId")UUID id , CustomerDTO customer){

        customerService.patchCustomerById(id,customer) ;
            return new ResponseEntity(HttpStatus.NO_CONTENT);

    }



}
