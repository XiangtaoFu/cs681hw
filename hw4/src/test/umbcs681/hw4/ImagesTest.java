package umbcs681.hw4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.function.Function;

public class ImagesTest {
    
    @Test
    public void testGrayscaleTransformation() {
        Image img = new Image(2, 2);
        img.setColor(0, 0, new Color(100, 150, 200));
        img.setColor(0, 1, new Color(50, 100, 150));
        img.setColor(1, 0, new Color(200, 100, 50));
        img.setColor(1, 1, new Color(80, 120, 180));
        
        Function<Color, Color> grayscaleAdjuster = (color) -> {
            int gray = (color.getRedScale() + color.getGreenScale() + color.getBlueScale()) / 3;
            return new Color(gray, gray, gray);
        };
        
        Image result = Images.transform(img, grayscaleAdjuster);
        
        assertEquals(150, result.getColor(0, 0).getRedScale());
        assertEquals(150, result.getColor(0, 0).getGreenScale());
        assertEquals(150, result.getColor(0, 0).getBlueScale());
        
        assertEquals(100, result.getColor(0, 1).getRedScale());
        assertEquals(100, result.getColor(0, 1).getGreenScale());
        assertEquals(100, result.getColor(0, 1).getBlueScale());
        
        assertEquals(116, result.getColor(1, 0).getRedScale());
        assertEquals(116, result.getColor(1, 0).getGreenScale());
        assertEquals(116, result.getColor(1, 0).getBlueScale());
        
        assertEquals(126, result.getColor(1, 1).getRedScale());
        assertEquals(126, result.getColor(1, 1).getGreenScale());
        assertEquals(126, result.getColor(1, 1).getBlueScale());
    }
    
    @Test
    public void testBinarizationTransformation() {
        Image img = new Image(2, 2);
        img.setColor(0, 0, new Color(100, 150, 200));
        img.setColor(0, 1, new Color(50, 100, 150));
        img.setColor(1, 0, new Color(200, 100, 50));
        img.setColor(1, 1, new Color(80, 120, 180));
        
        Function<Color, Color> binarizationAdjuster = (color) -> {
            int gray = (color.getRedScale() + color.getGreenScale() + color.getBlueScale()) / 3;
            if (gray <= 127) {
                return new Color(0, 0, 0);
            } else {
                return new Color(255, 255, 255);
            }
        };
        
        Image result = Images.transform(img, binarizationAdjuster);
        
        assertEquals(255, result.getColor(0, 0).getRedScale());
        assertEquals(255, result.getColor(0, 0).getGreenScale());
        assertEquals(255, result.getColor(0, 0).getBlueScale());
        assertEquals(0, result.getColor(0, 1).getRedScale());
        assertEquals(0, result.getColor(0, 1).getGreenScale());
        assertEquals(0, result.getColor(0, 1).getBlueScale());
        assertEquals(0, result.getColor(1, 0).getRedScale());
        assertEquals(0, result.getColor(1, 0).getGreenScale());
        assertEquals(0, result.getColor(1, 0).getBlueScale());
        assertEquals(0, result.getColor(1, 1).getRedScale());
        assertEquals(0, result.getColor(1, 1).getGreenScale());
        assertEquals(0, result.getColor(1, 1).getBlueScale());
    }
    
    @Test
    public void testIdentityTransformation() {
        Image img = new Image(2, 2);
        img.setColor(0, 0, new Color(100, 150, 200));
        img.setColor(0, 1, new Color(50, 100, 150));
        img.setColor(1, 0, new Color(200, 100, 50));
        img.setColor(1, 1, new Color(80, 120, 180));
        
        Function<Color, Color> identityAdjuster = (color) -> color;
        
        Image result = Images.transform(img, identityAdjuster);
        
        assertEquals(img.getColor(0, 0), result.getColor(0, 0));
        assertEquals(img.getColor(0, 1), result.getColor(0, 1));
        assertEquals(img.getColor(1, 0), result.getColor(1, 0));
        assertEquals(img.getColor(1, 1), result.getColor(1, 1));
    }
    
    @Test
    public void testStreamAPIUsage() {
        Image img = new Image(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                img.setColor(i, j, new Color(i * 50, j * 50, (i + j) * 25));
            }
        }
        
        Function<Color, Color> brightnessAdjuster = (color) -> {
            int newRed = Math.min(255, color.getRedScale() + 50);
            int newGreen = Math.min(255, color.getGreenScale() + 50);
            int newBlue = Math.min(255, color.getBlueScale() + 50);
            return new Color(newRed, newGreen, newBlue);
        };
        
        Image result = Images.transform(img, brightnessAdjuster);
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Color original = img.getColor(i, j);
                Color transformed = result.getColor(i, j);
                
                assertEquals(Math.min(255, original.getRedScale() + 50), transformed.getRedScale());
                assertEquals(Math.min(255, original.getGreenScale() + 50), transformed.getGreenScale());
                assertEquals(Math.min(255, original.getBlueScale() + 50), transformed.getBlueScale());
            }
        }
    }
}
