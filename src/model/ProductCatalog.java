package model;

import java.util.List;
import java.util.Map;

public record ProductCatalog(
        Map<Integer, Product> productsBySku,
        Map<String, List<Integer>> skusByType,
        List<Integer> allSkus
) {}
