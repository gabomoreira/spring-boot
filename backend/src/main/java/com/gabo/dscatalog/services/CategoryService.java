package com.gabo.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabo.dscatalog.dto.CategoryDTO;
import com.gabo.dscatalog.entities.Category;
import com.gabo.dscatalog.repositories.CategoryRepository;
import com.gabo.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();
		
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category category = obj.orElseThrow(() -> new EntityNotFoundException("Entity not Found"));
		
		return new CategoryDTO(category);
	}
	
	public CategoryDTO insert(CategoryDTO categoryDto) {
		Category category = new Category();
		category.setName(categoryDto.getName());
		category = repository.save(category);
		
		return new CategoryDTO(category);
	}
	
}
