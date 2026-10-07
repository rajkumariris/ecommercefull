package com.trading.ecommercefull.config;

import com.trading.ecommercefull.model.Product;
import com.trading.ecommercefull.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            log.info("Database already seeded with {} products.", productRepository.count());
            return;
        }

        log.info("Seeding initial product catalog...");

        List<Product> initialProducts = List.of(
                Product.builder()
                        .name("Sony WH-1000XM5 Wireless Headphones")
                        .description("Industry-leading noise cancelation optimized to you with two processors and 8 microphones. High-resolution audio, crystal-clear hands-free calling, and up to 30 hours battery life.")
                        .price(new BigDecimal("399.99"))
                        .category("Electronics")
                        .stockQuantity(45)
                        .imageUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80")
                        .rating(4.8)
                        .reviewCount(342)
                        .active(true)
                        .build(),

                Product.builder()
                        .name("Apple MacBook Air 15-inch M3")
                        .description("Strikingly thin design with blazing-fast Apple M3 chip, Liquid Retina display, MagSafe charging, and up to 18 hours of battery life.")
                        .price(new BigDecimal("1299.00"))
                        .category("Electronics")
                        .stockQuantity(20)
                        .imageUrl("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800&q=80")
                        .rating(4.9)
                        .reviewCount(215)
                        .active(true)
                        .build(),

                Product.builder()
                        .name("Keychron Q1 Pro Mechanical Keyboard")
                        .description("Full aluminum CNC body, hot-swappable switches, wireless Bluetooth 5.1 and QMK/VIA programmable custom mechanical keyboard.")
                        .price(new BigDecimal("199.50"))
                        .category("Electronics")
                        .stockQuantity(35)
                        .imageUrl("https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800&q=80")
                        .rating(4.7)
                        .reviewCount(180)
                        .active(true)
                        .build(),

                Product.builder()
                        .name("Dell UltraSharp 27 4K USB-C Monitor")
                        .description("Experience brilliant color and clarity with IPS Black technology, 98% DCI-P3 color coverage, and 90W USB-C single cable power delivery.")
                        .price(new BigDecimal("579.99"))
                        .category("Electronics")
                        .stockQuantity(18)
                        .imageUrl("https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800&q=80")
                        .rating(4.6)
                        .reviewCount(92)
                        .active(true)
                        .build(),

                Product.builder()
                        .name("Classic Minimalist Wool Overcoat")
                        .description("Crafted from 100% premium Italian wool blend, featuring modern tailored silhouette, horn buttons, and silky cupro lining.")
                        .price(new BigDecimal("289.00"))
                        .category("Fashion")
                        .stockQuantity(60)
                        .imageUrl("https://images.unsplash.com/photo-1539533018447-63fcce667883?w=800&q=80")
                        .rating(4.7)
                        .reviewCount(124)
                        .active(true)
                        .build(),

                Product.builder()
                        .name("Organic Cotton Heavyweight Hoodie")
                        .description("Relaxed fit made with 450 GSM organic French terry cotton. Ultra-soft interior with pre-shrunk durability and ribbed side panels.")
                        .price(new BigDecimal("85.00"))
                        .category("Fashion")
                        .stockQuantity(110)
                        .imageUrl("https://images.unsplash.com/photo-1556905055-8f358a7a47b2?w=800&q=80")
                        .rating(4.6)
                        .reviewCount(256)
                        .active(true)
                        .build(),

                Product.builder()
                        .name("Vintage Leather Heritage Boots")
                        .description("Full-grain waterproof leather with Goodyear welt construction, storm welt stitching, and rugged Vibram lug outsoles.")
                        .price(new BigDecimal("245.00"))
                        .category("Fashion")
                        .stockQuantity(40)
                        .imageUrl("https://images.unsplash.com/photo-1520639888713-7851133b1ed0?w=800&q=80")
                        .rating(4.8)
                        .reviewCount(168)
                        .active(true)
                        .build(),

                Product.builder()
                        .name("Ergonomic Mesh Task Chair")
                        .description("Breathable elastomeric mesh suspension, adjustable PostureFit lumbar support, and fully customizable 3D armrests.")
                        .price(new BigDecimal("695.00"))
                        .category("Home & Living")
                        .stockQuantity(15)
                        .imageUrl("https://images.unsplash.com/photo-1580481077195-c328a37db714?w=800&q=80")
                        .rating(4.9)
                        .reviewCount(520)
                        .active(true)
                        .build(),

                Product.builder()
                        .name("Smart Ambient LED Desk Lamp")
                        .description("Dual light source with auto-dimming ambient brightness sensor, color temperature tuning 2700K-6500K, and wireless charging base.")
                        .price(new BigDecimal("89.99"))
                        .category("Home & Living")
                        .stockQuantity(75)
                        .imageUrl("https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=800&q=80")
                        .rating(4.5)
                        .reviewCount(88)
                        .active(true)
                        .build(),

                Product.builder()
                        .name("Artisan Ceramic Pour-Over Coffee Set")
                        .description("Handcrafted matte ceramic dripper with insulated double-wall borosilicate glass carafe and precision walnut handle.")
                        .price(new BigDecimal("64.00"))
                        .category("Home & Living")
                        .stockQuantity(50)
                        .imageUrl("https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=800&q=80")
                        .rating(4.8)
                        .reviewCount(145)
                        .active(true)
                        .build(),

                Product.builder()
                        .name("Water-Resistant Modular Travel Backpack")
                        .description("Engineered with waterproof ballistic nylon, lockable YKK AquaGuard zippers, padded 16-inch laptop compartment, and load-lifter straps.")
                        .price(new BigDecimal("189.00"))
                        .category("Accessories")
                        .stockQuantity(30)
                        .imageUrl("https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=800&q=80")
                        .rating(4.9)
                        .reviewCount(310)
                        .active(true)
                        .build(),

                Product.builder()
                        .name("3-in-1 MagSafe Wireless Fast Charger")
                        .description("Simultaneous 15W fast wireless charging for phone, watch, and earbuds with weighted space gray aluminum base.")
                        .price(new BigDecimal("119.99"))
                        .category("Accessories")
                        .stockQuantity(85)
                        .imageUrl("https://images.unsplash.com/photo-1622445262464-84b1456045b6?w=800&q=80")
                        .rating(4.6)
                        .reviewCount(195)
                        .active(true)
                        .build()
        );

        productRepository.saveAll(initialProducts);
        log.info("Successfully seeded {} products into catalog.", initialProducts.size());
    }
}
