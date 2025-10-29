package umbcs681.hw5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BostonDataProcessor {
    
    public static void main(String[] args) {
        try {
            // Load Boston housing data from CSV file
            Path path = Paths.get("src/main/umbcs681/hw5/boston_housing.csv");
            List<BostonData> bostonData;
            
            try (Stream<String> lines = Files.lines(path)) {
                // Skip the header line and parse the CSV data
                bostonData = lines.skip(1)
                        .map(line -> {
                            String[] values = line.split(",");
                            // Clean neighborhood name (remove quotes if present)
                            String neighborhood = values[0].trim().replaceAll("^'|'$", "");
                            String propertyType = values[1].trim().replaceAll("^'|'$", "");
                            String condition = values[7].trim().replaceAll("^'|'$", "");
                            
                            return new BostonData(
                                    neighborhood,                                    // neighborhood
                                    propertyType,                                   // propertyType
                                    Double.parseDouble(values[2].trim()),           // price
                                    Integer.parseInt(values[3].trim()),             // bedrooms
                                    Integer.parseInt(values[4].trim()),             // bathrooms
                                    Double.parseDouble(values[5].trim()),           // squareFeet
                                    Integer.parseInt(values[6].trim()),             // yearBuilt
                                    condition,                                       // condition
                                    Double.parseDouble(values[8].trim()),           // latitude
                                    Double.parseDouble(values[9].trim())            // longitude
                            );
                        })
                        .collect(Collectors.toList());
            }
            
            System.out.println("Loaded " + bostonData.size() + " Boston housing data records from CSV file.");
            
            // Data Processing 1: Group by neighborhood and calculate average price (using groupingBy)
            System.out.println("\n--- Data Processing 1: Average Price by Neighborhood (using groupingBy) ---");
            Map<String, Double> avgPriceByNeighborhood = bostonData.stream()
                    .collect(Collectors.groupingBy(
                            BostonData::getNeighborhood,
                            Collectors.averagingDouble(BostonData::getPrice)
                    ));
            
            avgPriceByNeighborhood.forEach((neighborhood, avgPrice) -> 
                System.out.printf("%s: $%.2f average price\n", neighborhood, avgPrice));
            
            // Data Processing 2: Partition data by property condition (using partitioningBy)
            System.out.println("\n--- Data Processing 2: Properties by Condition (using partitioningBy) ---");
            Map<Boolean, List<BostonData>> dataByCondition = bostonData.stream()
                    .collect(Collectors.partitioningBy(data -> data.getCondition().equals("Excellent")));
            
            System.out.println("Excellent Condition Properties:");
            dataByCondition.get(true).forEach(data -> 
                System.out.printf("  %s: $%.2f (%d bedrooms, %d bathrooms)\n", 
                        data.getNeighborhood(), data.getPrice(), data.getBedrooms(), data.getBathrooms()));
            
            System.out.println("\nNon-Excellent Condition Properties:");
            dataByCondition.get(false).forEach(data -> 
                System.out.printf("  %s: $%.2f (%d bedrooms, %d bathrooms)\n", 
                        data.getNeighborhood(), data.getPrice(), data.getBedrooms(), data.getBathrooms()));
            
            // Data Processing 3: Reduce operation to find most expensive property (using reduce)
            System.out.println("\n--- Data Processing 3: Most Expensive Property (using reduce) ---");
            BostonData mostExpensive = bostonData.stream()
                    .reduce((data1, data2) -> data1.getPrice() > data2.getPrice() ? data1 : data2)
                    .orElse(null);
            
            if (mostExpensive != null) {
                System.out.printf("Most Expensive Property:\n");
                System.out.printf("  Neighborhood: %s\n", mostExpensive.getNeighborhood());
                System.out.printf("  Property Type: %s\n", mostExpensive.getPropertyType());
                System.out.printf("  Price: $%.2f\n", mostExpensive.getPrice());
                System.out.printf("  Bedrooms: %d\n", mostExpensive.getBedrooms());
                System.out.printf("  Bathrooms: %d\n", mostExpensive.getBathrooms());
                System.out.printf("  Square Feet: %.2f\n", mostExpensive.getSquareFeet());
                System.out.printf("  Year Built: %d\n", mostExpensive.getYearBuilt());
                System.out.printf("  Condition: %s\n", mostExpensive.getCondition());
            }
            
            // Additional analysis: Calculate statistics for property prices
            System.out.println("\n--- Additional Analysis: Property Price Statistics ---");
            DoubleSummaryStatistics priceStats = bostonData.stream()
                    .collect(Collectors.summarizingDouble(BostonData::getPrice));
            
            System.out.printf("Property Price Statistics:\n");
            System.out.printf("  Average: $%.2f\n", priceStats.getAverage());
            System.out.printf("  Minimum: $%.2f\n", priceStats.getMin());
            System.out.printf("  Maximum: $%.2f\n", priceStats.getMax());
            System.out.printf("  Total: $%.2f\n", priceStats.getSum());
            System.out.printf("  Count: %d\n", priceStats.getCount());
            
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
