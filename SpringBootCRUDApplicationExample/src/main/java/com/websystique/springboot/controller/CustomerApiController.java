package com.websystique.springboot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.websystique.springboot.model.Customers;
import com.websystique.springboot.service.CustomerService;
import com.websystique.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/")
public class CustomerApiController {

	public static final Logger logger = LoggerFactory.getLogger(CustomerApiController.class);

	@Autowired
	CustomerService customerService; // Service which will do all data
										// retrieval/manipulation work

	// -------------------Retrieve All
	// Customerss---------------------------------------------

	@RequestMapping(value = "/customer/", method = RequestMethod.GET)
	public ResponseEntity<List<Customers>> listAllCustomerss() {
		List<Customers> customers = customerService.findAllCustomers();
		if (customers.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Customers>>(customers, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Customers------------------------------------------

	@RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomers(@PathVariable("id") int id) {
		logger.info("Fetching Customers with id {}", id);
		Customers customer = customerService.findById(id);
		if (customer == null) {
			logger.error("Customers with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Customers with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Customers>(customer, HttpStatus.OK);
	}

	// -------------------Create a
	// Customers-------------------------------------------

	@RequestMapping(value = "/customer/", method = RequestMethod.POST)
	public ResponseEntity<?> createCustomers(@RequestBody Customers customer, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Customers : {}", customer);

		if (customerService.isCustomerExist(customer)) {
			logger.error("Unable to create. A Customers with name {} already exist", customer.getCustomername());
			return new ResponseEntity(new CustomErrorType(
					"Unable to create. A Customers with name " + customer.getCustomername() + " already exist."),
					HttpStatus.CONFLICT);
		}
		customerService.saveCustomer(customer);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/customer/{id}").buildAndExpand(customer.getCustomernumber()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Customers
	// ------------------------------------------------

	@RequestMapping(value = "/customer/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCustomers(@PathVariable("id") int id, @RequestBody Customers customer) {
		logger.info("Updating Customers with id {}", id);

		Customers currentCustomers = customerService.findById(id);

		if (currentCustomers == null) {
			logger.error("Unable to update. Customers with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Customers with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentCustomers.setCustomername(customer.getCustomername());
		currentCustomers.setContactlastname(customer.getContactlastname());
		currentCustomers.setContactfirstname(customer.getContactfirstname());

		customerService.updateCustomer(currentCustomers);
		return new ResponseEntity<Customers>(currentCustomers, HttpStatus.OK);
	}

	// ------------------- Delete a
	// Customers-----------------------------------------

	@RequestMapping(value = "/customer/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCustomers(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting Customers with id {}", id);

		Customers customer = customerService.findById(id);
		if (customer == null) {
			logger.error("Unable to delete. Customers with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Customers with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		customerService.deleteCustomerById(id);
		return new ResponseEntity<Customers>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Customerss-----------------------------

	@RequestMapping(value = "/customer/", method = RequestMethod.DELETE)
	public ResponseEntity<Customers> deleteAllCustomerss() {
		logger.info("Deleting All Customerss");

		customerService.deleteAllCustomers();
		return new ResponseEntity<Customers>(HttpStatus.NO_CONTENT);
	}

}