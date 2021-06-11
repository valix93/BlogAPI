package it.rdev.blog.api.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.rdev.blog.api.controller.dto.TagDTO;
import it.rdev.blog.api.dao.TagDao;
import it.rdev.blog.api.dao.entity.Tag;
import it.rdev.blog.api.service.TagDetailsService;

@Service
public class BlogTagDetailsService implements TagDetailsService{
	
	@Autowired
	private TagDao tagDao;

	@Override
	public Set<TagDTO> findAll() {
		Iterable<Tag> tags = tagDao.findAll();
		Set<TagDTO> listaTags = null;
		if (tags!=null) {
			listaTags = new HashSet<>();
			for (Tag t : tags) {	
				TagDTO tagDTO = new TagDTO();
				tagDTO.setTitolo(t.getTitolo());
				listaTags.add(tagDTO);
			}
		}
		return listaTags;
	}
	
	@Override
	public Tag save(TagDTO tag) {
		Tag newTag = new Tag();
		newTag.setTitolo(tag.getTitolo());
		return tagDao.save(newTag);
	}
	
}