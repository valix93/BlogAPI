package it.rdev.blog.api.controller;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	// TODO migliorare questo metodo, favorire il riutilizzo del codice
	@RequestMapping(value = "/api/articolo", method = RequestMethod.GET)
	public ResponseEntity<Set<ArticoloDTO>> find(
			@RequestHeader(required = false, value = "Authorization") String token,
			@RequestParam(required = false) Map<String, String> req){
		ResponseEntity<Set<ArticoloDTO>> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Set<ArticoloDTO> articoli = null;
		if (req==null || req.isEmpty()) {
			articoli = service.findAll();
		}
		else {
			for (String  r : req.keySet()) {
				String testo = req.get(r);
				if (r.equals("testo")) {
					articoli = service.findArticoloByTesto(testo);
				}
				// TODO fare in and le altre operazioni
				if (r.equals("id")) {
					Integer id = Integer.parseInt(testo);
					ArticoloDTO articolo = service.findArticoloById(id);
					articoli.add(articolo);
				}
			}
		}
		if (articoli!=null && !articoli.isEmpty()) {
			Iterator<ArticoloDTO> it = articoli.iterator();
			if(token != null && token.startsWith("Bearer")) {
				token = token.replaceAll("Bearer ", "");
				long idUtente = jwtUtil.getUserIdFromToken(token);
				while (it.hasNext()) {
					ArticoloDTO a = it.next(); 
					if (a.getAutore().getId()!=idUtente && a.getData_pubblicazione()==null) {
					    it.remove(); // previene la ConcurrentModificationException
						articoli.remove(a);
					}
				}
			}
			else {
				while (it.hasNext()) {
				   ArticoloDTO a = it.next(); 
				   if (a.getData_pubblicazione()==null) {
					      it.remove(); // previene la ConcurrentModificationException
					      articoli.remove(a);
				   }
				}
			}
			if (articoli!=null && !articoli.isEmpty()) response = new ResponseEntity<>(articoli, HttpStatus.OK);
			else response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return response;
	}
	
	@RequestMapping(value = "/api/articolo/{id:\\d+}")
	public ResponseEntity<?> getArticoloById(@PathVariable final long id, 
			@RequestHeader(required = false, value = "Authorization") String token) {
		ResponseEntity<ArticoloDTO> response;
		ArticoloDTO articolo = service.findArticoloById(id);
		if (articolo.getData_pubblicazione()==null) {
			if(token != null && token.startsWith("Bearer")) {
				token = token.replaceAll("Bearer ", "");
				long idUtente = jwtUtil.getUserIdFromToken(token);
				if (articolo.getAutore().getId()!=idUtente) {
					articolo = null;
				}
			}
			else {
				response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return response;
			}

		}
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