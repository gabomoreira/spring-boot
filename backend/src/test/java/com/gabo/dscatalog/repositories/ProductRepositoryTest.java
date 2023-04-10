package com.gabo.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.gabo.dscatalog.entities.Product;
import com.gabo.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository repository;

	private long existingId;
	private long notExistisId;
	private long countTotalProducts; 
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		notExistisId = 1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		 Optional<Product> entity = repository.findById(existingId);
		 
		 Assertions.assertFalse(entity.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdNotExists() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			
			repository.deleteById(notExistisId);
		});
	}
	
	@Test
	public void findByIdShouldReturnOptionalObjectWhenIdExists() {
		
		Optional<Product> obj = repository.findById(existingId);
		
		Assertions.assertTrue(obj.isPresent());
	}
	
	@Test
	public void findByIdShouldToBeEmptyWhenIdNotExists() {
		
		Optional<Product> obj = repository.findById(notExistisId);
		
		Assertions.assertFalse(obj.isPresent());
	}
}

















