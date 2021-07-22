package com.calidBurn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.calidBurn.entity.Product;
import com.calidBurn.entity.User;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	/**
	 * Find all {@link Product} published by the {@link User}
	 * @param username of the {@link User}
	 * @param pageable
	 * @return {@link Page} of {@link Product}
	 */
	@Query("SELECT p FROM User u JOIN u.published p WHERE u.username = :username")
	Page<Product> findAllPublishedByUsername(String username, Pageable pageable);

	/**
	 * Find all {@link Product} purchased by the {@link User}
	 * @param username of the {@link User}
	 * @param pageable
	 * @return {@link Page} of {@link Product}
	 */
	@Query("SELECT p FROM User u JOIN u.purchased p WHERE u.username = :username")
	Page<Product> findAllPurchasedByUsername(String username, Pageable pageable);

	/**
	 * Know if one {@link Product} is purchased by the {@link User}
	 * @param {@link Product} to know if is purchased
	 * @param username of the {@link User}
	 * @return true if the {@link Product} was purchased by the {@link User}
	 */
	@Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END "
		+  "FROM User u JOIN u.purchased p "
		+  "WHERE p.id = :id AND u.username = :username")
	boolean isPurchased(int id, String username);

}
