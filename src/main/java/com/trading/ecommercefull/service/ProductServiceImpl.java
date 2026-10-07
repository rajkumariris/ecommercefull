package com.trading.ecommercefull.service;

import com.trading.ecommercefull.dto.ProductRequest;
import com.trading.ecommercefull.dto.ProductResponse;
import com.trading.ecommercefull.exception.ResourceNotFoundException;
import com.trading.ecommercefull.model.Product;
import com.trading.ecommercefull.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all active products");
        return productRepository.findByActiveTrue()
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @Override
    public Page<ProductResponse> getProductsPaged(Pageable pageable) {
        log.info("Fetching products page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return productRepository.findByActiveTrue(pageable)
                .map(ProductResponse::fromEntity);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return ProductResponse.fromEntity(product);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String category) {
        log.info("Fetching products in category: {}", category);
        return productRepository.findByCategoryIgnoreCaseAndActiveTrue(category)
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @Override
    public Page<ProductResponse> getProductsByCategoryPaged(String category, Pageable pageable) {
        log.info("Fetching products in category: {} with paging", category);
        return productRepository.findByCategoryIgnoreCaseAndActiveTrue(category, pageable)
                .map(ProductResponse::fromEntity);
    }

    @Override
    public List<ProductResponse> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProducts();
        }
        log.info("Searching products with query: {}", query);
        return productRepository.searchProducts(query.trim())
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @Override
    public List<String> getAllCategories() {
        log.info("Fetching all distinct categories");
        return productRepository.findDistinctCategories();
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating new product: {}", request.getName());
        Product product = Product.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory().trim())
                .stockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : 0)
                .imageUrl(request.getImageUrl())
                .rating(request.getRating() != null ? request.getRating() : 0.0)
                .reviewCount(request.getReviewCount() != null ? request.getReviewCount() : 0)
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        Product saved = productRepository.save(product);
        log.info("Product created successfully with id: {}", saved.getId());
        return ProductResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory().trim());
        product.setStockQuantity(request.getStockQuantity());

        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }
        if (request.getRating() != null) {
            product.setRating(request.getRating());
        }
        if (request.getReviewCount() != null) {
            product.setReviewCount(request.getReviewCount());
        }
        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }

        Product updated = productRepository.save(product);
        log.info("Product with id: {} updated successfully", updated.getId());
        return ProductResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
        log.info("Product with id: {} deleted successfully", id);
    }
}
