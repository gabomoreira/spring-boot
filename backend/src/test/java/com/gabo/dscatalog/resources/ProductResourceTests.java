package com.gabo.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabo.dscatalog.dto.ProductDTO;
import com.gabo.dscatalog.services.ProductService;
import com.gabo.dscatalog.services.exceptions.DatabaseException;
import com.gabo.dscatalog.services.exceptions.ResourceNotFoundException;
import com.gabo.dscatalog.tests.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Long existingId;
	private Long notExistingId;
	private Long dependentId;
	private ProductDTO productDto;
	private PageImpl<ProductDTO> page;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		notExistingId = 2L;
		dependentId = 3L;
		productDto = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDto));
		
		// when(service.findAllPaged(any())).thenReturn(page);
		
		when(service.findById(existingId)).thenReturn(productDto);
		when(service.findById(notExistingId)).thenThrow(ResourceNotFoundException.class);
		
		when(service.insert(any())).thenReturn(productDto);
		
		when(service.update(any(), eq(existingId))).thenReturn(productDto);
		when(service.update(any(), eq(notExistingId))).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(service).deleteById(existingId);
		doThrow(ResourceNotFoundException.class).when(service).deleteById(notExistingId);
		doThrow(DatabaseException.class).when(service).deleteById(dependentId);
	}
	
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/products")
								.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenExistingId() throws Exception {

		ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
								.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenNotExistingId() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/products/{id}", notExistingId)
								.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenExistingId() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenNotExistingId() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", notExistingId)
				.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldDoNothingWhenExistingId() throws Exception {
		
		ResultActions result = mockMvc.perform(delete("/products/{id}", existingId));
		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldThrowNotFoundWhenNotExistingId() throws Exception {
		
		ResultActions result = mockMvc.perform(delete("/products/{id}", notExistingId));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldThrowIntegrityViolationtWhenDependentExistingId() throws Exception {
		
		ResultActions result = mockMvc.perform(delete("/products/{id}", dependentId));
		
		result.andExpect(status().isBadRequest());
	}
	
	@Test
	public void insertShouldReturnProductDTOWhenExistingProductDTO() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc.perform(post("/products")
				.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	
}























