package stats;

import java.time.LocalDate;

public class CustomerSummary {

    public final LocalDate date;
    public final int storeId;
    public final int customerId;

    public boolean boughtMilk;
    public boolean boughtCereal;
    public boolean boughtBabyFood;
    public boolean boughtDiapers;
    public boolean boughtBread;
    public boolean boughtPB;
    public boolean boughtJam;

    public CustomerSummary(LocalDate date, int storeId, int customerId) {
        this.date = date;
        this.storeId = storeId;
        this.customerId = customerId;
    }
}
