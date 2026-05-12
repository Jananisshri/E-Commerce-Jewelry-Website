package com.tanishq.service;

import com.tanishq.dto.PriceDetailsDTO;
import com.tanishq.dto.ProductDTO;
import com.tanishq.exception.ResourceNotFoundException;
import com.tanishq.model.Product;
import com.tanishq.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GoldPriceService goldPriceService;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return toDTO(product);
    }

    public List<ProductDTO> getProductsByCategory(String category) {
        List<Product> products = productRepository.findTop8ByCategory(category);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found for category: " + category);
        }
        return products.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String keyword) {
        return productRepository.searchByName(keyword)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PriceDetailsDTO getPriceDetails(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        BigDecimal goldRatePerGram = goldPriceService.getGoldPricePerGram(product.getKaratage());

        BigDecimal goldValue    = product.getWeight()
                                    .multiply(goldRatePerGram)
                                    .setScale(2, RoundingMode.HALF_UP);

        BigDecimal makingValue  = goldValue
                                    .multiply(product.getMakingPercent())
                                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal wastageValue = goldValue
                                    .multiply(product.getWastagePercent())
                                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal subTotal     = goldValue.add(makingValue).add(wastageValue);

        BigDecimal gstAmount    = subTotal
                                    .multiply(product.getGstPercent())
                                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal totalPrice   = subTotal.add(gstAmount);

        return PriceDetailsDTO.builder()
                .productId(product.getId())
                .productName(product.getName())
                .karatage(product.getKaratage())
                .weight(product.getWeight())
                .materials(product.getMaterials())
                .goldRatePerGram(goldRatePerGram)
                .goldPriceSource("MetalpriceAPI (live)")
                .goldValue(goldValue)
                .makingPercent(product.getMakingPercent())
                .makingValue(makingValue)
                .wastagePercent(product.getWastagePercent())
                .wastageValue(wastageValue)
                .subTotal(subTotal)
                .gstPercent(product.getGstPercent())
                .gstAmount(gstAmount)
                .totalPrice(totalPrice)
                .build();
    }

    private ProductDTO toDTO(Product p) {
        return ProductDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .category(p.getCategory())
                .imageUrl(p.getImageUrl())
                .weight(p.getWeight())
                .karatage(p.getKaratage())
                .materials(p.getMaterials())
                .wastagePercent(p.getWastagePercent())
                .makingPercent(p.getMakingPercent())
                .gstPercent(p.getGstPercent())
                .isFeatured(p.getIsFeatured())
                .build();
    }
}