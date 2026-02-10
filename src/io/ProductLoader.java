package io;

import model.Product;
import model.ProductCatalog;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class ProductLoader {

    private ProductLoader() {}

    public static ProductCatalog load(Path path) throws Exception {
        Map<Integer, Product> productsBySku = new HashMap<>(4096);
        Map<String, List<Integer>> skusByType = new HashMap<>();
        List<Integer> allSkus = new ArrayList<>(4096);

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {
            String header = reader.readLine();
            if (header == null) {
                throw new IllegalStateException("Products file is empty");
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] cols = line.split("\\|", -1);
                if (cols.length < 6) continue;

                String manufacturer = cols[0].trim();
                String name = cols[1].trim();
                String size = cols[2].trim();
                String type = cols[3].trim();
                int sku = Integer.parseInt(cols[4].trim());
                double basePrice = parsePrice(cols[5]);

                Product product = new Product(
                        sku,
                        name,
                        manufacturer,
                        size,
                        type,
                        basePrice
                );

                productsBySku.put(sku, product);
                allSkus.add(sku);
                skusByType.computeIfAbsent(type, k -> new ArrayList<>()).add(sku);
            }
        }

        if (productsBySku.isEmpty()) {
            throw new IllegalStateException("No products loaded from file");
        }

        allSkus.sort(Comparator.naturalOrder());
        for (List<Integer> list : skusByType.values()) {
            list.sort(Comparator.naturalOrder());
        }

        return new ProductCatalog(productsBySku, skusByType, allSkus);
    }

    private static double parsePrice(String raw) {
        String s = raw.trim();
        if (s.startsWith("$")) {
            s = s.substring(1);
        }
        if (s.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(s);
    }
}
