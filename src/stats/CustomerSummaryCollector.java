package stats;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CustomerSummaryCollector {

    private final Map<String, CustomerSummary> map = new HashMap<>();

    public void mark(String key, CustomerSummary summary) {
        map.putIfAbsent(key, summary);
    }

    public void export(Path path) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(path)) {
            w.write("date,storeId,customerId,boughtMilk,boughtCereal,boughtBabyFood,boughtDiapers,boughtBread,boughtPB,boughtJam\n");
            for (CustomerSummary s : map.values()) {
                w.write(String.format(
                        "%s,%d,%d,%d,%d,%d,%d,%d,%d,%d\n",
                        s.date,
                        s.storeId,
                        s.customerId,
                        s.boughtMilk ? 1 : 0,
                        s.boughtCereal ? 1 : 0,
                        s.boughtBabyFood ? 1 : 0,
                        s.boughtDiapers ? 1 : 0,
                        s.boughtBread ? 1 : 0,
                        s.boughtPB ? 1 : 0,
                        s.boughtJam ? 1 : 0
                ));
            }
        }
    }
}
