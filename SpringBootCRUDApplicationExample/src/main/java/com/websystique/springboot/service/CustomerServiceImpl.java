package com.websystique.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.websystique.springboot.model.Customers;
import com.websystique.springboot.repositories.CustomerRepository;

@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public Customers findById(Integer id) {
		return customerRepository.findOne(id);
	}

	public Customers findByName(String customername) {
		return customerRepository.findByCustomername(customername);
	}

	public void saveCustomer(Customers customer) {
		customerRepository.save(customer);
	}

	public void updateCustomer(Customers customer) {
		saveCustomer(customer);
	}

	public void deleteCustomerById(Integer id) {
		customerRepository.delete(id);
	}

	public void deleteAllCustomers() {
		customerRepository.deleteAll();
	}

	public List<Customers> findAllCustomers() {
		return customerRepository.findAll();
	}

	public boolean isCustomerExist(Customers customer) {
		return findByName(customer.getCustomername()) != null;
	}

}
