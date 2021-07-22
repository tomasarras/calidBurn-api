package com.calidBurn.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.calidBurn.entity.Product;
import com.calidBurn.service.ImageService;
import com.calidBurn.service.ProductService;

@Service
public class ImageServiceImpl implements ImageService {
	private String imagesFolder;
	
	private ProductService productService;
	
	
	@Autowired
	public ImageServiceImpl(
			@Value("${path.images.folder}") String imagesFolder,
			ProductService productService
			) {
		this.imagesFolder = imagesFolder;
		this.productService = productService;
		makeDirectoryIfNotExist(imagesFolder);
	}
	
	private void makeDirectoryIfNotExist(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

	@Override
	public Product uploadToProduct(MultipartFile image, Product product) {
		UUID randomUUID = UUID.randomUUID();
		String fileName = randomUUID.toString();
		String fileExtension = FilenameUtils.getExtension(image.getOriginalFilename());
		String completeFileName = fileName.concat(".").concat(fileExtension);
		String path = imagesFolder.concat("/").concat(completeFileName);
        Path fileNamePath = Paths.get(path);
        try {
        	Files.write(fileNamePath, image.getBytes());
        } catch (IOException e) {
        	System.out.println(e);
        }
        product.setImageName(completeFileName);
        return productService.update(product);
	}

	@Override
	public byte[] getImageByPath(String imageName) throws Exception {
		InputStream in = new FileInputStream(imagesFolder.concat("/").concat(imageName));
		return IOUtils.toByteArray(in);
	}

	
}
