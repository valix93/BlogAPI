package it.rdev.blog.api.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.rdev.blog.api.config.JwtTokenUtil;
import it.rdev.blog.api.controller.dto.ArticoloDTO;
import it.rdev.blog.api.service.impl.BlogArticoloDetailsService;

@RestController
public class ArticoloApiController {
	
	@Autowired
	private JwtTokenUtil jwtUtil;
	
	@Autowired
	private BlogArticoloDetailsService service;

	@RequestMapping(value = "/api/articolo", method = RequestMethod.GET)
	public ResponseEntity<Set<ArticoloDTO>> find(@RequestHeader(required = false, value = "Authorization") String token){
		ResponseEntity<Set<ArticoloDTO>> response;
		Set<ArticoloDTO> articoli = service.findAll();
		if (articoli==null || articoli.isEmpty()) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else {
			if(token != null && token.startsWith("Bearer")) {
				token = token.replaceAll("Bearer ", "");
				long idUtente = jwtUtil.getUserIdFromToken(token);
				for (ArticoloDTO a : articoli) {
					if (a.getAutore().getId()!=idUtente && a.getData_pubblicazione()==null) {
						System.out.println(a.getAutore().getId() + " id Autore");
						articoli.remove(a);
					}
				}
			}
			else {
				for (ArticoloDTO a : articoli) {
					if (a.getData_pubblicazione()==null) {
						articoli.remove(a);
					}
				}
			}
			response = new ResponseEntity<>(articoli, HttpStatus.OK);
		}
		return response;
	}
	
	//TODO bozza come in /api/articolo per non registrati / non proprietari e id non esistente
	@RequestMapping(value = "/api/articolo/{id:\\d+}")
	public ResponseEntity<?> getArticoloById(@PathVariable final long id, @RequestHeader(required = false, value = "Authorization") String token) {
		ResponseEntity<ArticoloDTO> response;
		ArticoloDTO articolo = service.findArticoloById(id);
		response = new ResponseEntity<>(articolo, HttpStatus.OK);
		return response;
	}
	
	@PostMapping({ "/api/articolo" })
	public String post(@RequestHeader(name = "Authorization") String token) {
		String username = null;
		if(token != null && token.startsWith("Bearer")) {
			token = token.replaceAll("Bearer ", "");
			username = jwtUtil.getUsernameFromToken(token);
		}
		return "Risorsa Protetta [" + username + "]";
	}

}