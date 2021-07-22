package com.calidBurn.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.calidBurn.entity.Product;
import com.calidBurn.entity.User;
import com.calidBurn.exception.ProductNotFoundException;
import com.calidBurn.repository.ProductRepository;
import com.calidBurn.service.ProductService;
import com.calidBurn.service.UserService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserService userService;
	
	@Override
	public Product create(Product product, String username) {
		User user = userService.getUserByUsername(username);
		product.setPublisher(user);
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllByPageAndSize(int page, int size) {
		final Pageable pageable = PageRequest.of(page, size);
		Page<Product> resultPage = productRepository.findAll(pageable);
		return resultPage.getContent();
	}

	@Override
	public Product getById(Integer id) throws ProductNotFoundException {
		try {
			return productRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ProductNotFoundException();
		}
	}

	@Override
	public Product edit(Product data, Product original) {
		Product product = Product.builder()
				.id(original.getId())
				.name(data.getName())
				.price(data.getPrice())
				.description(data.getDescription())
				.imageName(data.getImageName())
				.build();
		
		return productRepository.save(product);
	}

	@Override
	public void deleteById(Integer id) throws ProductNotFoundException {
		productRepository.deleteById(id);
	}

	@Override
	public User purchaseProduct(Integer productId, String username) throws ProductNotFoundException {
		Product product = getById(productId);
		User user = userService.getUserByUsername(username);
		user.purchaseProduct(product);
		return userService.update(user);
	}

	@Override
	public List<Product> findAllPublished(String username, int page, int size) {
		final Pageable pageable = PageRequest.of(page, size);
		Page<Product> resultPage = productRepository.findAllPublishedByUsername(username, pageable);
		return resultPage.getContent();
	}

	@Override
	public List<Product> findAllPurchased(String username, int page, int size) {
		final Pageable pageable = PageRequest.of(page, size);
		Page<Product> resultPage = productRepository.findAllPurchasedByUsername(username, pageable);
		return resultPage.getContent();
	}

	@Override
	public Product update(Product product) {
		return productRepository.save(product);
	}

	@Override
	public boolean isPurchased(int id, String username) {
		return productRepository.isPurchased(id, username);
	}
}
