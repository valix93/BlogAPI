package it.rdev.blog.api.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.rdev.blog.api.controller.dto.TagDTO;
import it.rdev.blog.api.service.impl.BlogTagDetailsService;

@RestController
public class TagApiController {
	
	@Autowired
	private BlogTagDetailsService service;

	@GetMapping({ "/api/tag" })
	public ResponseEntity<Set<TagDTO>> find(){
		ResponseEntity<Set<TagDTO>> response;
		Set<TagDTO> tags = service.findAll();
		if (tags.isEmpty() || tags.size()<1)
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		else
			response = new ResponseEntity<>(tags, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/api/tag", method = RequestMethod.POST)
	public ResponseEntity<?> saveTag(@RequestBody TagDTO tag) throws Exception {
		return ResponseEntity.ok(service.save(tag));
	}
}