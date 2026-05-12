package com.tanishq.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class GoldPriceService {

    private static final Logger log = LoggerFactory.getLogger(GoldPriceService.class);
    private static final BigDecimal TROY_OUNCE_TO_GRAM = new BigDecimal("31.1035");

    @Value("${gold.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoldPriceService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public BigDecimal getGoldPricePerGram(String karatage) {
        try {
            // MetalpriceAPI — base INR, get XAU price
            String url = "https://api.metalpriceapi.com/v1/latest"
                    + "?api_key=" + apiKey
                    + "&base=INR"
                    + "&currencies=XAU";

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JsonNode json = objectMapper.readTree(response.getBody());

            // XAU rate means: 1 INR = X troy ounces of gold
            // So 1 troy ounce = 1 / XAU rate in INR
            BigDecimal xauRate = new BigDecimal(json.get("rates").get("XAU").asText());
            BigDecimal pricePerOunceINR = BigDecimal.ONE.divide(xauRate, 10, RoundingMode.HALF_UP);
            BigDecimal pricePerGram24K  = pricePerOunceINR.divide(TROY_OUNCE_TO_GRAM, 4, RoundingMode.HALF_UP);

            BigDecimal purityFactor = getPurityFactor(karatage);
            BigDecimal result = pricePerGram24K.multiply(purityFactor).setScale(2, RoundingMode.HALF_UP);

            log.info("Live gold price per gram for {}: ₹{}", karatage, result);
            return result;

        } catch (Exception e) {
            log.warn("Live gold price fetch failed, using fallback. Error: {}", e.getMessage());
            return getFallbackPrice(karatage);
        }
    }

    private BigDecimal getPurityFactor(String karatage) {
        if (karatage == null) return BigDecimal.ONE;
        return switch (karatage.toUpperCase().trim()) {
            case "24K", "24KT" -> new BigDecimal("1.000");
            case "22K", "22KT" -> new BigDecimal("0.916");
            case "18K", "18KT" -> new BigDecimal("0.750");
            case "14K", "14KT" -> new BigDecimal("0.583");
            default             -> new BigDecimal("0.916");
        };
    }

    private BigDecimal getFallbackPrice(String karatage) {
        // Current approximate 24K gold price per gram in INR
        BigDecimal base24K = new BigDecimal("9500.00");
        return base24K.multiply(getPurityFactor(karatage)).setScale(2, RoundingMode.HALF_UP);
    }
}