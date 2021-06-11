package it.rdev.blog.api.service;

import java.util.Set;
import it.rdev.blog.api.controller.dto.TagDTO;
import it.rdev.blog.api.dao.entity.Tag;

public interface TagDetailsService {
	public Set<TagDTO> findAll();
	public Tag save(TagDTO tag);
}
