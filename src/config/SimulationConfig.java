package config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public final class SimulationConfig {
    private SimulationConfig() {}

    public static final int STORE_COUNT = 8;

    public static final LocalDate START_DATE = LocalDate.of(2025, 1, 1);
    public static final LocalDate END_DATE_INCLUSIVE = LocalDate.of(2025, 12, 31);

    public static final int WEEKDAY_CUSTOMERS_LOW_INCLUSIVE = 980;
    public static final int WEEKDAY_CUSTOMERS_HIGH_INCLUSIVE = 1020;
    public static final int WEEKEND_CUSTOMER_INCREASE = 75;

    public static final int ITEMS_PER_CUSTOMER_LOW_INCLUSIVE = 1;
    public static final int ITEMS_PER_CUSTOMER_HIGH_INCLUSIVE = 60;

    public static final double PRICE_MULTIPLIER = 1.10;

    public static final long GLOBAL_SEED = 8L;

    public static final Path PRODUCTS_FILE_PATH = Paths.get("Dataset", "Products1.txt");

    public static final boolean EXPORT_D3_JSON = true;
    public static final Path D3_OUTPUT_DIR = Paths.get("Dataset");

    public static final boolean SANITY_CHECK_ENABLED = true;

    public static final Path SANITY_CHECK_OUTPUT_PATH =
            Paths.get("Dataset", "customer_summary.csv");

    public static final String TYPE_MILK = "Milk";
    public static final String TYPE_CEREAL = "Cereal";
    public static final String TYPE_BABY_FOOD = "Baby Food";
    public static final String TYPE_DIAPERS = "Diapers";
    public static final String TYPE_BREAD = "Bread";
    public static final String TYPE_PEANUT_BUTTER = "Peanut Butter";
    public static final String TYPE_JELLY_JAM = "Jelly/Jam";

}
