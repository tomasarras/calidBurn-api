package com.calidBurn.service;

import java.util.List;

import com.calidBurn.entity.Product;
import com.calidBurn.entity.User;
import com.calidBurn.exception.ProductNotFoundException;

/**
 * Service for {@link Product}
 *
 */
public interface ProductService {
	/**
	 * Create one {@link Product}
	 * @param {@link Product} to create
	 * @param username of the owner
	 * @return {@link Product} created
	 */
	Product create(Product product, String username);
	
	/**
	 * Get all {@link Product} by page and size
	 * @param page
	 * @param size
	 * @return {@link List} of {@link Product}
	 */
	List<Product> getAllByPageAndSize(int page, int size);
	
	/**
	 * Get one {@link Product} by his id
	 * @param id of the {@link Product}
	 * @return {@link Product} found
	 * @throws {@link ProductNotFoundException}
	 */
	Product getById(Integer id) throws ProductNotFoundException;
	
	/**
	 * Edit one {@link Product} by his id
	 * @param {@link Product} data to edit
	 * @param id of the {@link Product} to be edited
	 * @return {@link Product} edited
	 */
	Product edit(Product data, Product original);
	
	/**
	 * Delete one {@link Product} by his id
	 * @param id of the {@link Product} to be deleted
	 * @throws {@link ProductNotFoundException}
	 */
	void deleteById(Integer id) throws ProductNotFoundException;
	
	/**
	 * Purchase one {@link Product}
	 * @param productId of the {@link Product} to purchase
	 * @param username of the {@link User} that buy the {@link Product}
	 * @return {@link User} that buy the {@link Product}
	 * @throws {@link ProductNotFoundException}
	 */
	User purchaseProduct(Integer productId, String username) throws ProductNotFoundException;

	/**
	 * Find all {@link Product} published by the {@link User}
	 * @param username of the {@link User}
	 * @param page
	 * @param size
	 * @return {@link List} of {@link Product}
	 */
	List<Product> findAllPublished(String username, int page, int size);

	/**
	 * Find all {@link Product} purchased by the {@link User}
	 * @param username of the {@link User}
	 * @param page
	 * @param size
	 * @return {@link List} of {@link Product}
	 */
	List<Product> findAllPurchased(String username, int page, int size);
	
	/**
	 * Update one {@link Product}
	 * @param {@link Product} to update
	 * @return {@link Product} updated
	 */
	Product update(Product product);

	/**
	 * Know if one {@link Product} is purchased by the {@link User}
	 * @param id of the {@link Product} to know if is purchased
	 * @param username of the {@link User}
	 * @return true if the {@link Product} was purchased by the {@link User}
	 */
	boolean isPurchased(int id, String username);

	
}
