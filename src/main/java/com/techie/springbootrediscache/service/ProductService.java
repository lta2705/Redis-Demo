package com.techie.springbootrediscache.service;

import com.techie.springbootrediscache.dto.ProductDto;
import com.techie.springbootrediscache.entity.Product;
import com.techie.springbootrediscache.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public static final String PRODUCT_CACHE = "products";
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @CachePut(value = PRODUCT_CACHE, key = "#result.id()")
    public ProductDto createProduct(ProductDto productDto) {
        Product product = new Product();
        product = modelMapper.map(productDto, product.getClass());
        productRepository.save(product);

        return modelMapper.map(product, ProductDto.class);
    }

    @Cacheable(value = PRODUCT_CACHE, key = "#productId")
    public ProductDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find product with id " + productId));

        return modelMapper.map(product, ProductDto.class);
    }

    @CachePut(value = PRODUCT_CACHE, key = "#result.id()")
    public ProductDto updateProduct(ProductDto productDto) {
        Long productId = productDto.getId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find product with id " + productId));

        modelMapper.map(productDto, product);
        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductDto.class);

    }

    @CacheEvict(value = PRODUCT_CACHE, key = "#productId")
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
