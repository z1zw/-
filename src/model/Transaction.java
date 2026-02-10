package model;

import java.time.LocalDate;

public record Transaction(
        LocalDate date,
        int storeId,
        int customerId,
        int sku,
        double salePrice
) {
    public String toCsv() {
        return date + "," + storeId + "," + customerId + "," + sku + "," + salePrice;
    }
}
