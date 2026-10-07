package com.trading.ecommercefull.service;

import com.trading.ecommercefull.dto.ProductRequest;
import com.trading.ecommercefull.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAllProducts();

    Page<ProductResponse> getProductsPaged(Pageable pageable);

    ProductResponse getProductById(Long id);

    List<ProductResponse> getProductsByCategory(String category);

    Page<ProductResponse> getProductsByCategoryPaged(String category, Pageable pageable);

    List<ProductResponse> searchProducts(String query);

    List<String> getAllCategories();

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
}
