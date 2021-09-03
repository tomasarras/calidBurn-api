package com.calidBurn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.calidBurn.entity.Product;
import com.calidBurn.entity.User;
import com.calidBurn.exception.ProductNotFoundException;
import com.calidBurn.service.ImageService;
import com.calidBurn.service.ProductService;

/**
 * Controller to manage images
 *
 */
@RestController
@RequestMapping("/images")
public class ImageController {
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ProductService productService;
	
	/**
	 * Upload one image to one {@link Product}
	 * @param productId of the {@link Product}
	 * @param image to upload
	 * @param usename of the {@link User}
	 * @return {@link ResponseEntity} with {@link HttpStatus#OK} and {@link Product} with image uploaded
	 * @return {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND} {@link Product} not found
	 */
	@PostMapping("/products/{productId}")
	public ResponseEntity<Product> uploadToOneProduct(
			@PathVariable int productId,
			@RequestParam("image") MultipartFile image,
			@AuthenticationPrincipal String usename
			) {
		
		try {
			Product product = productService.getById(productId);
			
			return ResponseEntity.ok()
					.body(imageService.uploadToProduct(image, product));
		} catch (ProductNotFoundException e) {
			return ResponseEntity.notFound().build();
		}

	}
	
	/**
	 * Get one image by the imageName
	 * @param imageName of the image
	 * @return image in bytes
	 */
	@GetMapping(value = "/products/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getByImageName(@PathVariable String imageName) {
		try {
			return imageService.getImageByPath(imageName);
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}
	
	/**
	 * Upload one signature image to one {@link Product}
	 * @param productId of the {@link Product}
	 * @param image to upload
	 * @param usename of the {@link User}
	 * @return {@link ResponseEntity} with {@link HttpStatus#OK} and {@link Product} with image uploaded
	 * @return {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND} {@link Product} not found
	 */
	@PostMapping("/products/{productId}/signature")
	public ResponseEntity<Void> uploadSignatureToOneProduct(
			@PathVariable int productId,
			@RequestParam("image") MultipartFile image,
			@AuthenticationPrincipal String usename
			) {
		
		try {
			Product product = productService.getById(productId);
			imageService.uploadSignatureToProduct(image, product);
			return ResponseEntity.ok().build();
		} catch (ProductNotFoundException e) {
			return ResponseEntity.notFound().build();
		}

	}
}
