package com.shopping.service.Impl;

import com.shopping.entity.Product;
import com.shopping.repository.ProductRepository;
import com.shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);

    }
}
