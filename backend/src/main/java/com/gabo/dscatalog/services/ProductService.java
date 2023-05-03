package com.gabo.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabo.dscatalog.dto.CategoryDTO;
import com.gabo.dscatalog.dto.ProductDTO;
import com.gabo.dscatalog.entities.Category;
import com.gabo.dscatalog.entities.Product;
import com.gabo.dscatalog.repositories.CategoryRepository;
import com.gabo.dscatalog.repositories.ProductRepository;
import com.gabo.dscatalog.services.exceptions.DatabaseException;
import com.gabo.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<ProductDTO> findAll() {
		List<Product> list = repository.findAll();
		
		return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Long categoryId, String name, Pageable pageable) {
		Category category = categoryId == 0 ? null : categoryRepository.getOne(categoryId);
		Page<Product> list = repository.find(category, name, pageable);
		
		return list.map(x -> new ProductDTO(x, x.getCategories()));
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product product = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not Found"));
		
		return new ProductDTO(product, product.getCategories());
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO productDto) {
		Product product = new Product(); 
		copyDtoToEntity(productDto, product);
		product = repository.save(product);
		
		return new ProductDTO(product);
	}

	@Transactional
	public ProductDTO update(ProductDTO productDto, Long id) {
		try {
			Product product = repository.getOne(id);
			copyDtoToEntity(productDto, product);
			product = repository.save(product);
			
			return new ProductDTO(product);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void deleteById(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	
	public void copyDtoToEntity(ProductDTO productDto, Product product) {
		product.setName(productDto.getName());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setImgUrl(productDto.getImgUrl());
		product.setDate(productDto.getDate());
		
		product.getCategories().clear();
		for ( CategoryDTO categoryDto : productDto.getCategories()) {
			Category category = categoryRepository.getOne(categoryDto.getId());
			product.getCategories().add(category);
		}
	}
}













