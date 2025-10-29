package umbcs681.hw3;

import java.util.ArrayList;
import java.util.List;

public class CarProcessor {
    
    // Find the lowest price using 2nd version of reduce()
    public static Integer findMinPrice(List<Car> cars) {
        return cars.stream()
                .map(Car::getPrice)
                .reduce(Integer.MAX_VALUE, (result, price) -> price < result ? price : result);
    }

    // Find the highest price using 2nd version of reduce()
    public static Integer findMaxPrice(List<Car> cars) {
        return cars.stream()
                .map(Car::getPrice)
                .reduce(Integer.MIN_VALUE, (result, price) -> price > result ? price : result);
    }

    // Compute the average price using 3rd version of reduce() with ArrayList
    public static double computeAveragePrice(List<Car> cars) {
        List<Integer> result = cars.stream()
                .map(Car::getPrice)
                .reduce(
                        new ArrayList<>(List.of(0, 0)), // [sum, count] using ArrayList
                        (acc, price) -> {
                            acc.set(0, acc.get(0) + price); // sum += price
                            acc.set(1, acc.get(1) + 1);     // count += 1
                            return acc;
                        },
                        (finalRes, interRes) -> {
                            // Combine results from different streams if parallel
                            finalRes.set(0, finalRes.get(0) + interRes.get(0)); // Combine sums
                            finalRes.set(1, finalRes.get(1) + interRes.get(1)); // Combine counts
                            return finalRes;
                        }
                );
        return result.get(1) == 0 ? 0 : (double) result.get(0) / result.get(1);
    }

    public static void main(String[] args) {
        List<Car> cars = List.of(
                new Car("Tesla", 50000),
                new Car("BMW", 45000),
                new Car("Toyota", 30000),
                new Car("Honda", 27000)
        );

        System.out.println("Lowest Price: " + findMinPrice(cars));
        System.out.println("Highest Price: " + findMaxPrice(cars));
        System.out.println("Average Price: " + computeAveragePrice(cars));
    }
}
