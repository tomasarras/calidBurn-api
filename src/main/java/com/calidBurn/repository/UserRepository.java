package com.calidBurn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.calidBurn.entity.Product;
import com.calidBurn.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

	/**
	 * Find one {@link User} owner of the {@link Product}
	 * @param productId of the {@link Product} to find
	 * @return {@link Optional} of {@link User} owner of the {@link Product}
	 */
	@Query("SELECT u FROM User u JOIN u.published p WHERE p.id = :productId")
	Optional<User> findByProductId(Integer productId);
}
