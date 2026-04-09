package com.tribalcrafts.tribalcrafts_backend.config;

import com.tribalcrafts.tribalcrafts_backend.entity.Product;
import com.tribalcrafts.tribalcrafts_backend.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Inserts the default tribal crafts catalog when the database has no products yet.
 * Image paths match the Vite app public folder: /images/...
 */
@Configuration
public class ProductCatalogSeed {

    @Bean
    public CommandLineRunner seedProducts(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() > 0) {
                return;
            }
            productRepository.save(product(
                    "Tribal Terracotta Pottery",
                    250.0,
                    "/images/Tribal Terracotta Pottery.jpg",
                    "Warli Pottery Collective",
                    "Handcrafted terracotta pots using ancient tribal techniques. Each pot is sun-dried and features traditional Warli art patterns depicting daily tribal life.",
                    "Pottery & Clay",
                    4.8,
                    24));
            productRepository.save(product(
                    "Bamboo Weaving Basket",
                    350.0,
                    "/images/Bamboo Weaving Basket.jpg",
                    "Bamboo Weavers Tribe",
                    "Traditional bamboo basket woven by tribal artisans using sustainable harvesting methods. Perfect for storage and decorative purposes.",
                    "Weaving & Basketry",
                    4.6,
                    18));
            productRepository.save(product(
                    "Tribal Wood Carving Art",
                    750.0,
                    "/images/Tribal Wood Carving Art.jpg",
                    "Gond Wood Carvers",
                    "Intricate wood carving depicting tribal deities and nature spirits. Hand-carved from sustainable teak wood using traditional tools.",
                    "Wood Carving",
                    4.9,
                    32));
            productRepository.save(product(
                    "Handwoven Tribal Shawl",
                    550.0,
                    "/images/Handwoven Tribal Shawl.jpeg",
                    "Naga Weavers Community",
                    "Warm handwoven shawl with traditional tribal patterns. Made using natural dyes from plants and minerals found in tribal regions.",
                    "Textiles & Weaving",
                    4.7,
                    29));
            productRepository.save(product(
                    "Tribal Beaded Jewelry",
                    150.0,
                    "/images/Tribal Beaded Jewelry.jpeg",
                    "Bhil Bead Artists",
                    "Authentic tribal jewelry made with natural beads, seeds, and semi-precious stones. Each piece tells a story of tribal heritage.",
                    "Jewelry & Accessories",
                    4.8,
                    41));
            productRepository.save(product(
                    "Traditional Tribal Drum",
                    200.0,
                    "/images/Traditional Tribal Drum.jpeg",
                    "Santhal Drum Makers",
                    "Handcrafted tribal drum used in traditional ceremonies and festivals. Made from natural materials with authentic tribal artwork.",
                    "Musical Instruments",
                    4.9,
                    15));
        };
    }

    private static Product product(String name, double price, String imageUrl, String artisan,
                                   String description, String category, double rating, int reviews) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setImageUrl(imageUrl);
        p.setArtisan(artisan);
        p.setDescription(description);
        p.setCategory(category);
        p.setRating(rating);
        p.setReviews(reviews);
        return p;
    }
}
