package stats;

import model.Product;
import model.ProductCatalog;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public final class Aggregator {

    private final ProductCatalog catalog;

    private long totalCustomers;
    private long totalItems;
    private double totalSales;

    private final Map<Integer, Long> skuCounts;
    private final Map<LocalDate, Map<Integer, Long>> dailySkuCounts = new HashMap<>();


    public Aggregator(ProductCatalog catalog) {
        this.catalog = catalog;
        this.totalCustomers = 0L;
        this.totalItems = 0L;
        this.totalSales = 0.0;
        this.skuCounts = new HashMap<>(4096);
    }

    public void addCustomers(int count) {
        totalCustomers += count;
    }

    public void accept(
            LocalDate date,
            int storeId,
            int customerId,
            int sku,
            double salePrice
    ) {
        totalItems++;
        totalSales += salePrice;
        skuCounts.merge(sku, 1L, Long::sum);
        dailySkuCounts
                .computeIfAbsent(date, d -> new HashMap<>())
                .merge(sku, 1L, Long::sum);
    }
    public Map<LocalDate, Map<Integer, Long>> dailySkuCounts() {
        return dailySkuCounts;
    }

    public void printSummary() {
        NumberFormat intFmt = NumberFormat.getIntegerInstance();
        NumberFormat moneyFmt = NumberFormat.getCurrencyInstance(Locale.US);

        System.out.println();
        System.out.println("===== SIMULATION SUMMARY =====");
        System.out.println("Total Customers : " + intFmt.format(totalCustomers));
        System.out.println("Total Items     : " + intFmt.format(totalItems));
        System.out.println("Total Sales     : " + moneyFmt.format(round2(totalSales)));
        System.out.println();

        System.out.println("Top 10 Items (by count)");
        System.out.println("--------------------------------------------");

        List<Map.Entry<Integer, Long>> top10 = getTop10();

        int rank = 1;
        for (Map.Entry<Integer, Long> e : top10) {
            Product p = catalog.productsBySku().get(e.getKey());
            String name = p != null ? p.name() : "UNKNOWN";
            System.out.printf(
                    "%2d. SKU=%d  Count=%s  Name=%s%n",
                    rank++,
                    e.getKey(),
                    intFmt.format(e.getValue()),
                    name
            );
        }
        System.out.println();
    }

    public List<Map.Entry<Integer, Long>> getTop10() {
        return skuCounts.entrySet()
                .stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(10)
                .collect(Collectors.toList());
    }

    public void exportD3Json(Path outputDir) throws Exception {
        exportSummaryJson(outputDir.resolve("summary.json"));
        exportTop10Json(outputDir.resolve("top10.json"));
    }

    private void exportSummaryJson(Path path) throws Exception {
        try (BufferedWriter w = Files.newBufferedWriter(path)) {
            w.write("{\n");
            w.write("  \"totalCustomers\": " + totalCustomers + ",\n");
            w.write("  \"totalItems\": " + totalItems + ",\n");
            w.write("  \"totalSales\": " + round2(totalSales) + "\n");
            w.write("}\n");
        }
    }

    private void exportTop10Json(Path path) throws Exception {
        List<Map.Entry<Integer, Long>> top10 = getTop10();

        try (BufferedWriter w = Files.newBufferedWriter(path)) {
            w.write("[\n");
            for (int i = 0; i < top10.size(); i++) {
                Map.Entry<Integer, Long> e = top10.get(i);
                Product p = catalog.productsBySku().get(e.getKey());

                w.write("  {\n");
                w.write("    \"rank\": " + (i + 1) + ",\n");
                w.write("    \"sku\": " + e.getKey() + ",\n");
                w.write("    \"count\": " + e.getValue() + ",\n");
                w.write("    \"name\": \"" + escape(p != null ? p.name() : "UNKNOWN") + "\"\n");
                w.write("  }");
                if (i < top10.size() - 1) {
                    w.write(",");
                }
                w.write("\n");
            }
            w.write("]\n");
        }
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private static String escape(String s) {
        return s.replace("\"", "\\\"");
    }
}
