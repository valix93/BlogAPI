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

import it.rdev.blog.api.controller.dto.CategoriaDTO;
import it.rdev.blog.api.service.BlogCategoriaDetailsService;

@RestController
public class CategoriaApiController {
	
	@Autowired
	private BlogCategoriaDetailsService service;

	@GetMapping({ "/api/categoria" })
	public ResponseEntity<Set<CategoriaDTO>> find(){
		ResponseEntity<Set<CategoriaDTO>> response;
		Set<CategoriaDTO> categorie = service.findAll();
		if (categorie.isEmpty() || categorie.size()<1)
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		else
			response = new ResponseEntity<>(categorie, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/api/categoria", method = RequestMethod.POST)
	public ResponseEntity<?> saveCategoria(@RequestBody CategoriaDTO categoria) throws Exception {
		return ResponseEntity.ok(service.save(categoria));
	}
}