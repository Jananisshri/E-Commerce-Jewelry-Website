package com.tanishq.controller;

import com.itextpdf.text.DocumentException;
import com.tanishq.dto.ApiResponse;
import com.tanishq.dto.PriceDetailsDTO;
import com.tanishq.dto.ProductDTO;
import com.tanishq.service.PdfService;
import com.tanishq.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PdfService pdfService;

    // GET all products
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse<>(true, "Products fetched successfully", products));
    }

    // GET featured products — BEFORE /{id}
    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getFeaturedProducts() {
        List<ProductDTO> products = productService.getFeaturedProducts();
        return ResponseEntity.ok(new ApiResponse<>(true, "Featured products fetched successfully", products));
    }

    // GET search products — BEFORE /{id}
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> searchProducts(@RequestParam String keyword) {
        List<ProductDTO> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(new ApiResponse<>(true, "Search results fetched", products));
    }

    // GET products by category — BEFORE /{id}
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByCategory(@PathVariable String category) {
        List<ProductDTO> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(new ApiResponse<>(true, "Products fetched successfully", products));
    }

    // GET product by ID — AFTER all specific endpoints
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product fetched successfully", product));
    }

    // GET price details of a product
    @GetMapping("/{id}/price-details")
    public ResponseEntity<ApiResponse<PriceDetailsDTO>> getPriceDetails(@PathVariable Long id) {
        PriceDetailsDTO priceDetails = productService.getPriceDetails(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Price details fetched successfully", priceDetails));
    }

    
    // GET download PDF of price details
    @GetMapping("/{id}/download-pdf")
    public ResponseEntity<byte[]> downloadPriceDetailsPdf(@PathVariable Long id) throws IOException, DocumentException {
        PriceDetailsDTO priceDetails = productService.getPriceDetails(id);
        byte[] pdf = pdfService.generatePriceQuotePdf(priceDetails);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("Tanishq_Price_Details_" + id + ".pdf")
                        .build()
        );

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}