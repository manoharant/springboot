package com.websystique.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.websystique.springboot.model.Customers;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Integer> {

    Customers findByCustomername(String name);

}
