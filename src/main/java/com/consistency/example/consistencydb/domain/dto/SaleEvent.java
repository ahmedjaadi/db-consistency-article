package com.consistency.example.consistencydb.domain.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public record SaleEvent(Long id, EventType type) {
    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            System.out.println("Error toString");
            return null;
        }
    }

    public static SaleEvent toSaleEvent(String event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(event, SaleEvent.class);
        } catch (JsonProcessingException ex) {
            System.out.println("Error toSaleEvent");
            return null;
        }
    }
}