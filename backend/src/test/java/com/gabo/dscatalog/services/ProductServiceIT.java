package com.gabo.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.gabo.dscatalog.dto.ProductDTO;
import com.gabo.dscatalog.repositories.ProductRepository;
import com.gabo.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {

	@Autowired
	ProductService service;
	
	@Autowired
	ProductRepository repository;
	
	private Long existingId;
	private Long notExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {

		existingId = 1L;
		notExistingId = 1000L;
		countTotalProducts = 25L;
	}

	@Test
	public void deleteShouldDeleteProductWhenIdExists() {
		service.deleteById(existingId);
		
		Assertions.assertEquals(countTotalProducts - 1, repository.count());

	}
	
	@Test
	public void deleteShouldDeleteThrowResourceNotFoundExceptionWhenNotExistingId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.deleteById(notExistingId);
		});
	}
	
	/*
	 * 
	
	@Test
	public void findAllPagedShouldReturnPageWhenPage0Size10() {
		
		PageRequest pageRequest = PageRequest.of(0,  10);
		
		Page<ProductDTO> result =  service.findAllPaged(pageRequest);
	
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPage50Size10() {
		
		PageRequest pageRequest = PageRequest.of(50,  10);
		
		Page<ProductDTO> result =  service.findAllPaged(pageRequest);
	
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnOrderedPageWhenSortByName() {
		
		PageRequest pageRequest = PageRequest.of(0,  10, Sort.by("name"));
		
		Page<ProductDTO> result =  service.findAllPaged(pageRequest);
	
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}
	 * */
	
}













