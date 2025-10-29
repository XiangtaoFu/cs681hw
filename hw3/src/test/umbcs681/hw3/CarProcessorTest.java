package umbcs681.hw3;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

public class CarProcessorTest {

    @Test
    public void testFindMinPrice() {
        List<Car> cars = List.of(
                new Car("Tesla", 50000),
                new Car("BMW", 45000),
                new Car("Toyota", 30000),
                new Car("Honda", 27000)
        );
        assertEquals(27000, CarProcessor.findMinPrice(cars));
    }

    @Test
    public void testFindMaxPrice() {
        List<Car> cars = List.of(
                new Car("Tesla", 50000),
                new Car("BMW", 45000),
                new Car("Toyota", 30000),
                new Car("Honda", 27000)
        );
        assertEquals(50000, CarProcessor.findMaxPrice(cars));
    }

    @Test
    public void testComputeAveragePrice() {
        List<Car> cars = List.of(
                new Car("Tesla", 50000),
                new Car("BMW", 45000),
                new Car("Toyota", 30000),
                new Car("Honda", 27000)
        );
        assertEquals(38000.0, CarProcessor.computeAveragePrice(cars), 0.001);
    }
} 