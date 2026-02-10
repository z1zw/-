package stats;

import model.Product;
import model.ProductCatalog;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public final class Top10DailyExporter {

    private Top10DailyExporter() {}

    public static void export(
            Aggregator aggregator,
            ProductCatalog catalog,
            Path outputPath
    ) throws Exception {

        List<Integer> top10Skus = aggregator.getTop10()
                .stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<LocalDate> dates = aggregator.dailySkuCounts()
                .keySet()
                .stream()
                .sorted()
                .toList();

        try (BufferedWriter w = Files.newBufferedWriter(outputPath)) {
            w.write("{\n");
            w.write("  \"dates\": [\n");

            for (int i = 0; i < dates.size(); i++) {
                if (i > 0) w.write(",\n");
                w.write("    \"" + dates.get(i) + "\"");
            }

            w.write("\n  ],\n");
            w.write("  \"series\": [\n");

            for (int i = 0; i < top10Skus.size(); i++) {
                int sku = top10Skus.get(i);
                Product p = catalog.productsBySku().get(sku);

                if (i > 0) w.write(",\n");

                w.write("    {\n");
                w.write("      \"rank\": " + (i + 1) + ",\n");
                w.write("      \"sku\": " + sku + ",\n");
                w.write("      \"name\": \"" + escape(p != null ? p.name() : "UNKNOWN") + "\",\n");
                w.write("      \"values\": [\n");

                for (int j = 0; j < dates.size(); j++) {
                    if (j > 0) w.write(",\n");
                    long c = aggregator.dailySkuCounts()
                            .getOrDefault(dates.get(j), Map.of())
                            .getOrDefault(sku, 0L);
                    w.write("        " + c);
                }

                w.write("\n      ]\n");
                w.write("    }");
            }

            w.write("\n  ]\n");
            w.write("}\n");
        }
    }

    private static String escape(String s) {
        return s.replace("\"", "\\\"");
    }
}
