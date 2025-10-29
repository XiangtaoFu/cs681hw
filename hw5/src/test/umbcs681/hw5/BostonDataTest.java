package umbcs681.hw5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BostonDataTest {
    private List<BostonData> testData;
    
    @BeforeEach
    public void setUp() throws IOException {
        Path path = Paths.get("src/main/umbcs681/hw5/boston_housing.csv");
        try (Stream<String> lines = Files.lines(path)) {
            testData = lines.skip(1)
                    .map(line -> {
                        String[] values = line.split(",");
                        String neighborhood = values[0].trim().replaceAll("^'|'$", "");
                        String propertyType = values[1].trim().replaceAll("^'|'$", "");
                        String condition = values[7].trim().replaceAll("^'|'$", "");
                        return new BostonData(neighborhood, propertyType,
                                Double.parseDouble(values[2].trim()),
                                Integer.parseInt(values[3].trim()),
                                Integer.parseInt(values[4].trim()),
                                Double.parseDouble(values[5].trim()),
                                Integer.parseInt(values[6].trim()),
                                condition,
                                Double.parseDouble(values[8].trim()),
                                Double.parseDouble(values[9].trim()));
                    })
                    .collect(Collectors.toList());
        }
    }
    
    @Test
    public void testBostonDataCreation() {
        BostonData data = new BostonData("Back Bay", "Condo", 750000.0, 
                                         2, 2, 1200.0, 2010, "Excellent", 42.3501, -71.0826);
        
        assertEquals("Back Bay", data.getNeighborhood());
        assertEquals("Condo", data.getPropertyType());
        assertEquals(750000.0, data.getPrice(), 0.01);
        assertEquals(2, data.getBedrooms());
        assertEquals(2, data.getBathrooms());
        assertEquals(1200.0, data.getSquareFeet(), 0.01);
        assertEquals(2010, data.getYearBuilt());
        assertEquals("Excellent", data.getCondition());
        assertEquals(42.3501, data.getLatitude(), 0.0001);
        assertEquals(-71.0826, data.getLongitude(), 0.0001);
    }
    
    @Test
    public void testDataLoaded() {
        assertNotNull(testData);
        assertTrue(testData.size() > 0);
    }
    
    @Test
    public void testGroupByNeighborhood() {
        Map<String, Double> avgPriceByNeighborhood = testData.stream()
                .collect(Collectors.groupingBy(
                        BostonData::getNeighborhood,
                        Collectors.averagingDouble(BostonData::getPrice)
                ));
        
        assertNotNull(avgPriceByNeighborhood);
        assertTrue(avgPriceByNeighborhood.size() > 0);
        avgPriceByNeighborhood.values().forEach(avgPrice -> 
            assertTrue(avgPrice > 0)
        );
    }
    
    @Test
    public void testPartitionByCondition() {
        Map<Boolean, List<BostonData>> dataByCondition = testData.stream()
                .collect(Collectors.partitioningBy(data -> data.getCondition().equals("Excellent")));
        
        assertNotNull(dataByCondition);
        assertTrue(dataByCondition.containsKey(true));
        assertTrue(dataByCondition.containsKey(false));
        dataByCondition.get(true).forEach(data -> 
            assertEquals("Excellent", data.getCondition())
        );
        dataByCondition.get(false).forEach(data -> 
            assertNotEquals("Excellent", data.getCondition())
        );
    }
    
    @Test
    public void testFindMostExpensiveProperty() {
        BostonData mostExpensive = testData.stream()
                .reduce((data1, data2) -> data1.getPrice() > data2.getPrice() ? data1 : data2)
                .orElse(null);
        
        assertNotNull(mostExpensive);
        double maxPrice = mostExpensive.getPrice();
        testData.forEach(data -> assertTrue(data.getPrice() <= maxPrice));
    }
    
    @Test
    public void testPriceStatistics() {
        DoubleSummaryStatistics priceStats = testData.stream()
                .collect(Collectors.summarizingDouble(BostonData::getPrice));
        
        assertNotNull(priceStats);
        assertTrue(priceStats.getCount() > 0);
        assertTrue(priceStats.getAverage() > 0);
        assertTrue(priceStats.getMin() > 0);
        assertTrue(priceStats.getMax() > priceStats.getMin());
        assertTrue(priceStats.getMax() >= priceStats.getAverage());
        assertTrue(priceStats.getMin() <= priceStats.getAverage());
    }
    
    @Test
    public void testFilterByBedrooms() {
        int targetBedrooms = 3;
        List<BostonData> filteredData = testData.stream()
                .filter(data -> data.getBedrooms() >= targetBedrooms)
                .collect(Collectors.toList());
        
        filteredData.forEach(data -> 
            assertTrue(data.getBedrooms() >= targetBedrooms)
        );
    }
    
    @Test
    public void testToString() {
        BostonData data = new BostonData("Beacon Hill", "Townhouse", 1200000.0, 
                                         3, 2, 1800.0, 1900, "Good", 42.3588, -71.0707);
        
        String result = data.toString();
        assertNotNull(result);
        assertTrue(result.contains("Beacon Hill"));
        assertTrue(result.contains("Townhouse"));
        assertTrue(result.contains("1200000.0"));
    }
}

