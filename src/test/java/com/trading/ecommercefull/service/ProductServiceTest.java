package com.trading.ecommercefull.service;

import com.trading.ecommercefull.dto.ProductRequest;
import com.trading.ecommercefull.dto.ProductResponse;
import com.trading.ecommercefull.exception.ResourceNotFoundException;
import com.trading.ecommercefull.model.Product;
import com.trading.ecommercefull.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product sampleProduct;
    private ProductRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .id(1L)
                .name("Wireless Noise Canceling Headphones")
                .description("Premium sound and ANC")
                .price(new BigDecimal("299.99"))
                .category("Electronics")
                .stockQuantity(50)
                .imageUrl("https://example.com/headphones.jpg")
                .rating(4.8)
                .reviewCount(120)
                .active(true)
                .build();

        sampleRequest = ProductRequest.builder()
                .name("Wireless Noise Canceling Headphones")
                .description("Premium sound and ANC")
                .price(new BigDecimal("299.99"))
                .category("Electronics")
                .stockQuantity(50)
                .imageUrl("https://example.com/headphones.jpg")
                .rating(4.8)
                .reviewCount(120)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Should return all active products")
    void getAllProducts_ShouldReturnActiveProducts() {
        when(productRepository.findByActiveTrue()).thenReturn(List.of(sampleProduct));

        List<ProductResponse> products = productService.getAllProducts();

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Wireless Noise Canceling Headphones");
        assertThat(products.get(0).getPrice()).isEqualTo(new BigDecimal("299.99"));
        verify(productRepository, times(1)).findByActiveTrue();
    }

    @Test
    @DisplayName("Should return product by ID when product exists")
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductResponse response = productService.getProductById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo(sampleProduct.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product ID not found")
    void getProductById_WhenProductNotFound_ShouldThrowException() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found with id : '999'");
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should filter products by category")
    void getProductsByCategory_ShouldReturnFilteredProducts() {
        when(productRepository.findByCategoryIgnoreCaseAndActiveTrue("Electronics"))
                .thenReturn(List.of(sampleProduct));

        List<ProductResponse> results = productService.getProductsByCategory("Electronics");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getCategory()).isEqualTo("Electronics");
        verify(productRepository, times(1)).findByCategoryIgnoreCaseAndActiveTrue("Electronics");
    }

    @Test
    @DisplayName("Should search products by query keyword")
    void searchProducts_ShouldReturnMatchingProducts() {
        when(productRepository.searchProducts("headphones")).thenReturn(List.of(sampleProduct));

        List<ProductResponse> results = productService.searchProducts("headphones");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).contains("Headphones");
        verify(productRepository, times(1)).searchProducts("headphones");
    }

    @Test
    @DisplayName("Should create product and return saved response")
    void createProduct_ShouldSaveAndReturnResponse() {
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.createProduct(sampleRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(sampleRequest.getName());
        assertThat(response.getPrice()).isEqualTo(sampleRequest.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should update existing product")
    void updateProduct_WhenExists_ShouldUpdateAndReturnResponse() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductRequest updateReq = ProductRequest.builder()
                .name("Updated Headphones")
                .description("Updated description")
                .price(new BigDecimal("349.99"))
                .category("Audio")
                .stockQuantity(30)
                .build();

        ProductResponse response = productService.updateProduct(1L, updateReq);

        assertThat(response).isNotNull();
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    @DisplayName("Should delete product when ID exists")
    void deleteProduct_WhenExists_ShouldDelete() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        doNothing().when(productRepository).delete(sampleProduct);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(sampleProduct);
    }
}
