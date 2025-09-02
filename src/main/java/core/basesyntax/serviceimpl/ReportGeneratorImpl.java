package core.basesyntax.serviceimpl;

import core.basesyntax.db.Storage;
import core.basesyntax.service.ReportGenerator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ReportGeneratorImpl implements ReportGenerator {
    private static final String HEADER = "fruit,quantity";

    @Override
    public String getReport() {
        if (Storage.getFruits().isEmpty()) {
            return HEADER + System.lineSeparator();
        }
        Map<String, Integer> sortedFruits = new TreeMap<>(Storage.getFruits());
        String dataLines = sortedFruits.entrySet().stream()
                .map(entry -> entry.getKey() + "," + entry.getValue())
                .collect(Collectors.joining(System.lineSeparator()));
        return HEADER + System.lineSeparator() + dataLines + System.lineSeparator();
    }
}
