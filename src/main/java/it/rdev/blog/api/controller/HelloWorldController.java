package it.rdev.blog.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

	@RequestMapping({ "/api/hello" })
	public String firstPage() {
		return "Hello World";
	}

}