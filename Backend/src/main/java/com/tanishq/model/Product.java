package com.tanishq.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal weight;

    @Column(nullable = false)
    private String karatage;

    @Column(nullable = false)
    private String materials;

    @Column(name = "wastage_percent", precision = 5, scale = 2)
    private BigDecimal wastagePercent;

    @Column(name = "making_percent", precision = 5, scale = 2)
    private BigDecimal makingPercent;

    @Column(name = "gst_percent", precision = 5, scale = 2)
    private BigDecimal gstPercent;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;
}