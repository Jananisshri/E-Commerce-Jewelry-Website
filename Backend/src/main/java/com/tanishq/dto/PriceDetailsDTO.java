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
public class PriceDetailsDTO {
    private Long productId;
    private String productName;
    private String karatage;
    private String materials;
    private BigDecimal weight;

    // Live gold price
    private BigDecimal goldRatePerGram;
    private String goldPriceSource;

    // Price breakdown (matches Image 7 exactly)
    private BigDecimal goldValue;       // weight × goldRatePerGram
    private BigDecimal makingPercent;   // e.g. 12%
    private BigDecimal makingValue;     // goldValue × makingPercent / 100
    private BigDecimal wastagePercent;  // e.g. 5%
    private BigDecimal wastageValue;    // goldValue × wastagePercent / 100
    private BigDecimal subTotal;        // goldValue + makingValue + wastageValue
    private BigDecimal gstPercent;      // 3%
    private BigDecimal gstAmount;       // subTotal × gstPercent / 100
    private BigDecimal totalPrice;      // subTotal + gstAmount
}