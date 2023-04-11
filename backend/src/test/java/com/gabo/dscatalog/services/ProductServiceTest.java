package com.gabo.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gabo.dscatalog.dto.ProductDTO;
import com.gabo.dscatalog.entities.Category;
import com.gabo.dscatalog.entities.Product;
import com.gabo.dscatalog.repositories.CategoryRepository;
import com.gabo.dscatalog.repositories.ProductRepository;
import com.gabo.dscatalog.services.exceptions.DatabaseException;
import com.gabo.dscatalog.services.exceptions.ResourceNotFoundException;
import com.gabo.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long existingId;
	private long notExisting;
	private long dependentId;
	private PageImpl<Product> pageImpl;
	private Product product;
	private Category category;
	
	@BeforeEach
	void setUp() {
		existingId = 1L;
		notExisting = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		pageImpl = new PageImpl<>(List.of(product));
		category = Factory.createCategory();
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(pageImpl);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(notExisting)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.doThrow(ResourceNotFoundException.class).when(repository).getOne(notExisting);
		Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(notExisting);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenExistingId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			ProductDTO dto = service.update(Factory.createProductDTO(), notExisting);
			
			Assertions.assertNull(dto);
		});
		
		Mockito.verify(repository, Mockito.times(1)).getOne(notExisting);
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenExistingId() {
		
		ProductDTO dto = service.update(Factory.createProductDTO(), existingId);
		
		Assertions.assertNotNull(dto);
	
		Mockito.verify(repository, Mockito.times(1)).getOne(existingId);
		Mockito.verify(repository, Mockito.times(1)).save(product);
		Mockito.verify(categoryRepository, Mockito.times(1)).getOne(existingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWheNotExistingId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(notExisting);
		});
		
		Mockito.verify(repository, Mockito.times(1)).findById(notExisting);

	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenExistsId() {
		
		ProductDTO dto = service.findById(existingId);
		
		Assertions.assertNotNull(dto);
		
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);

	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> page = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(page);
		
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);

	}
	
	@Test
	public void deleteThrowDatabaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.deleteById(dependentId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);

	}
	
	@Test
	public void deleteThrowResourceNotFoundExceptionWhenNotExistsId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.deleteById(notExisting);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(notExisting);

	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.deleteById(existingId);
			
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	
}
