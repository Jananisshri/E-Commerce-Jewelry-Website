package com.tanishq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private String imageUrl;
    private BigDecimal weight;
    private String karatage;
    private String materials;
    private BigDecimal wastagePercent;
    private BigDecimal makingPercent;
    private BigDecimal gstPercent;
    private Boolean isFeatured;
}