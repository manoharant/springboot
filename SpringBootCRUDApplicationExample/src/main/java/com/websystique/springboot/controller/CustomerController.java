package com.websystique.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomerController {

	@RequestMapping("/cust/")
	String home(ModelMap modal) {
		System.out.println("inside cust list");
		modal.addAttribute("title", "CRUD Example");
		return "customer";
	}

}
