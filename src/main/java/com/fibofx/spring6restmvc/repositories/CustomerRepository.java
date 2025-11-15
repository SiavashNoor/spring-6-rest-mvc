package com.fibofx.spring6restmvc.repositories;

import com.fibofx.spring6restmvc.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


//JpaRepository has more features than the CRUD repositories ,
public interface CustomerRepository extends JpaRepository<Customer, UUID> {


}
