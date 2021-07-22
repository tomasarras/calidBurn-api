package com.calidBurn.service;


import org.springframework.web.multipart.MultipartFile;

import com.calidBurn.entity.Product;

public interface ImageService {
	/**
	 * Upload one image to one {@link Product}
	 * @param image to upload
	 * @param {@link Product}
	 * @return {@link Product} with image uploaded
	 */
	Product uploadToProduct(MultipartFile image, Product product);
	
	/**
	 * Get one image of one {@link Product}
	 * @param path of the image
	 * @return array of bytes of image
	 * @throws {@link Exception} image not found
	 */
	byte[] getImageByPath(String imageName) throws Exception;
}
