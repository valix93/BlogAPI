package it.rdev.blog.api.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.rdev.blog.api.controller.dto.TagDTO;
import it.rdev.blog.api.dao.TagDao;
import it.rdev.blog.api.dao.entity.Tag;

@Service
public class BlogTagDetailsService {
	
	@Autowired
	private TagDao tagDao;

	public Set<TagDTO> findAll() {
		Iterable<Tag> tags = tagDao.findAll();
		Set<TagDTO> listaTags = new HashSet<>();
		for (Tag t : tags) {			
			TagDTO tagDTO = new TagDTO();
			tagDTO.setTitolo(t.getTitolo());
			listaTags.add(tagDTO);
		}
		return listaTags;
	}
	
	public Tag save(TagDTO tag) {
		Tag newTag = new Tag();
		newTag.setTitolo(tag.getTitolo());
		return tagDao.save(newTag);
	}
	
}