package com.gabo.dscatalog.tests;

import java.time.Instant;

import com.gabo.dscatalog.dto.ProductDTO;
import com.gabo.dscatalog.entities.Category;
import com.gabo.dscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good phone x", 4170.0, "mimg url", Instant.parse("2020-07-14T10:00:00Z"));
		product.getCategories().add(new Category(2L, "Eletronics"));
		
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		
		return new ProductDTO(product, product.getCategories());
	}
}
