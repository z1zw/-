package model;

public record Product(
        int sku,
        String name,
        String manufacturer,
        String size,
        String type,
        double basePrice
) {}
