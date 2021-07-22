package com.calidBurn.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calidBurn.entity.Product;
import com.calidBurn.entity.User;
import com.calidBurn.exception.ProductNotFoundException;
import com.calidBurn.service.ProductService;

/**
 * Controller for {@link Product}
 *
 */
@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductService productService;
	
	/**
	 * Create one {@link Product} 
	 * @param {@link Product} 
	 * @param username of the {@link User} that create the {@link Product} 
	 * @return {@link ResponseEntity} with {@link HttpStatus#CREATED} and the {@link Product} created
	 */
	@PostMapping
	public ResponseEntity<Product> create(
			@RequestBody Product product,
			@AuthenticationPrincipal String username
			) {
		
		return ResponseEntity.status(Response.SC_CREATED)
				.body(productService.create(product, username));
	}
	
	/**
	 * Get all {@link Product} 
	 * @param page
	 * @param size
	 * @return {@link ResponseEntity} with {@link HttpStatus#OK} and {@link List} of {@link Product}
	 */
	@GetMapping
	public ResponseEntity<List<Product>> getAllByPageAndSize(
			@RequestParam int page,
			@RequestParam int size
			) {
		return ResponseEntity.ok(productService.getAllByPageAndSize(page, size));
	}
	
	/**
	 * Get one {@link Product} 
	 * @param id of the {@link Product} 
	 * @param username of the {@link User} to know if the product is purchased by him
	 * @return {@link ResponseEntity} with {@link HttpStatus#OK} and the {@link Product}
	 * @return {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Product> getByid(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(productService.getById(id));
		} catch (ProductNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Edit one {@link Product} if is owner of him
	 * @param id of the {@link Product} to edit
	 * @param {@link Product} with data to edit
	 * @param username of owner
	 * @return {@link ResponseEntity} with {@link HttpStatus#OK} and the {@link Product} edited
	 * @return {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND}
	 * @return {@link ResponseEntity} with {@link HttpStatus#FORBIDDEN} if is not the owner of the {@link Product}
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Product> editById(
			@PathVariable Integer id,
			@RequestBody Product data,
			@AuthenticationPrincipal String username
			) {
		try {
			Product product = productService.getById(id);
			if (product.getPublisher().getUsername().equals(username)) {
				return ResponseEntity.ok(productService.edit(data, product));
			} else {
				return ResponseEntity.status(Response.SC_FORBIDDEN).build();
			}
		} catch (ProductNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Delete one {@link Product} by id
	 * @param id of the {@link Product} to delete
	 * @param username of the {@link User} owner of the {@link Product}
	 * @return {@link ResponseEntity} with {@link HttpStatus#NO_CONTENT} if the {@link Product} was deleted
	 * @return {@link ResponseEntity} with {@link HttpStatus#FORBIDDEN} if is not the owner of the {@link Product}
	 * @return {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(
			@PathVariable Integer id,
			@AuthenticationPrincipal String username
			) {
		
		try {
			Product product = productService.getById(id);
			if (product.getPublisher().getUsername().equals(username)) {
				productService.deleteById(id);
				return ResponseEntity.status(Response.SC_NO_CONTENT).build();
			} else {
				return ResponseEntity.status(Response.SC_FORBIDDEN).build();
			}
		} catch (ProductNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
}
	
	/**
	 * Purchase one {@link Product}
	 * @param id of the {@link Product} to purchase
	 * @param username of the {@link User} that purchase the {@link Product}
	 * @return {@link ResponseEntity} with {@link HttpStatus#NO_CONTENT} if was purchased
	 * @return {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND}
	 */
	@PostMapping("/{id}/purchase")
	public ResponseEntity<Void> purchase(
			@PathVariable Integer id,
			@AuthenticationPrincipal String username
			) {
		try {
			productService.purchaseProduct(id, username);
			return ResponseEntity.status(Response.SC_NO_CONTENT).build();
		} catch (ProductNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Get all {@link Product} published by the {@link User}
	 * @param page
	 * @param size
	 * @param username of the {@link User}
	 * @return {@link ResponseEntity} with {@link HttpStatus#OK}
	 */
	@GetMapping("/published")
	public ResponseEntity<List<Product>> getPublished(
			@RequestParam int page,
			@RequestParam int size,
			@AuthenticationPrincipal String username
			) {
		
		return ResponseEntity.ok(productService.findAllPublished(username, page, size));
	}
	
	/**
	 * Get all {@link Product} purchased by the {@link User}
	 * @param page
	 * @param size
	 * @param username of the {@link User}
	 * @return {@link ResponseEntity} with {@link HttpStatus#OK}
	 */
	@GetMapping("/purchased")
	public ResponseEntity<List<Product>> getPurchased(
			@RequestParam int page,
			@RequestParam int size,
			@AuthenticationPrincipal String username
			) {
		
		return ResponseEntity.ok(productService.findAllPurchased(username, page, size));
	}
	
	/**
	 * Know if one {@link Product} is purchased by the {@link User}
	 * @param id of the {@link Product}
	 * @param username of the {@link User}
	 * @return {@link ResponseEntity} with {@link HttpStatus#OK} if is purchased
	 * @return {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND} if not purchased
	 */
	@GetMapping("/{id}/purchased")
	public ResponseEntity<Void> isPurchased (
			@PathVariable int id,
			@AuthenticationPrincipal String username
			) {
		
		if (productService.isPurchased(id, username))
			return ResponseEntity.ok().build();
		else
			return ResponseEntity.notFound().build();
		
	}

}
