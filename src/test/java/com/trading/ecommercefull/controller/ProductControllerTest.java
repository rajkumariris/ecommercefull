package com.trading.ecommercefull.controller;

import tools.jackson.databind.ObjectMapper;
import com.trading.ecommercefull.dto.ProductRequest;
import com.trading.ecommercefull.dto.ProductResponse;
import com.trading.ecommercefull.exception.GlobalExceptionHandler;
import com.trading.ecommercefull.exception.ResourceNotFoundException;
import com.trading.ecommercefull.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper;
    private ProductResponse sampleResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        sampleResponse = ProductResponse.builder()
                .id(1L)
                .name("Wireless Noise Canceling Headphones")
                .description("High-end noise canceling")
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
    @DisplayName("GET /api/products should return list of products")
    void getAllProducts_ShouldReturnProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is("Wireless Noise Canceling Headphones")))
                .andExpect(jsonPath("$.data[0].price", is(299.99)));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("GET /api/products?category=Electronics should return category products")
    void getProductsByCategory_ShouldReturnFilteredProducts() throws Exception {
        when(productService.getProductsByCategory("Electronics")).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/api/products").param("category", "Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].category", is("Electronics")));

        verify(productService, times(1)).getProductsByCategory("Electronics");
    }

    @Test
    @DisplayName("GET /api/products/{id} should return single product")
    void getProductById_WhenFound_ShouldReturnProduct() throws Exception {
        when(productService.getProductById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Wireless Noise Canceling Headphones")));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    @DisplayName("GET /api/products/{id} should return 404 when not found")
    void getProductById_WhenNotFound_ShouldReturn404() throws Exception {
        when(productService.getProductById(999L))
                .thenThrow(new ResourceNotFoundException("Product", "id", 999L));

        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Product not found with id : '999'")));

        verify(productService, times(1)).getProductById(999L);
    }

    @Test
    @DisplayName("POST /api/products should create product and return 201")
    void createProduct_WithValidData_ShouldReturn201() throws Exception {
        ProductRequest request = ProductRequest.builder()
                .name("Wireless Noise Canceling Headphones")
                .description("High-end noise canceling")
                .price(new BigDecimal("299.99"))
                .category("Electronics")
                .stockQuantity(50)
                .build();

        when(productService.createProduct(any(ProductRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Wireless Noise Canceling Headphones")));

        verify(productService, times(1)).createProduct(any(ProductRequest.class));
    }

    @Test
    @DisplayName("POST /api/products with invalid data should return 400 Bad Request")
    void createProduct_WithInvalidData_ShouldReturn400() throws Exception {
        ProductRequest invalidRequest = ProductRequest.builder()
                .name("")
                .price(new BigDecimal("-10.00"))
                .category("")
                .stockQuantity(-5)
                .build();

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.data.name", notNullValue()))
                .andExpect(jsonPath("$.data.price", notNullValue()))
                .andExpect(jsonPath("$.data.category", notNullValue()))
                .andExpect(jsonPath("$.data.stockQuantity", notNullValue()));

        verify(productService, never()).createProduct(any(ProductRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} should delete product and return 200")
    void deleteProduct_ShouldReturn200() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("deleted successfully")));

        verify(productService, times(1)).deleteProduct(1L);
    }
}
