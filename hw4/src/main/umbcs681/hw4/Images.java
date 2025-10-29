package umbcs681.hw4;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Images {
    
    /**
     * Transform an image using Stream API and a Function for color adjustment
     * @param image the input image
     * @param adjuster a Function that takes a Color and returns an adjusted Color
     * @return the transformed image
     */
    public static Image transform(Image image, Function<Color, Color> adjuster) {
        // Use Stream API to apply adjuster on each pixel in image
        List<List<Color>> transformedPixels = image.getPixels().stream()
                .map(row -> row.stream()
                        .map(adjuster)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        
        // Create new image with transformed pixels
        Image result = new Image(image.getHeight(), image.getWidth());
        result.setPixels(transformedPixels);
        return result;
    }
    
    public static void main(String[] args) {
        // Create a test image
        Image img = new Image(3, 3);
        
        // Set some test colors
        img.setColor(0, 0, new Color(100, 150, 200));
        img.setColor(0, 1, new Color(50, 100, 150));
        img.setColor(0, 2, new Color(200, 100, 50));
        img.setColor(1, 0, new Color(80, 120, 180));
        img.setColor(1, 1, new Color(30, 80, 130));
        img.setColor(1, 2, new Color(180, 80, 30));
        img.setColor(2, 0, new Color(120, 200, 100));
        img.setColor(2, 1, new Color(70, 150, 80));
        img.setColor(2, 2, new Color(220, 150, 70));
        
        System.out.println("Original Image:");
        System.out.println(img);
        
        // Grayscale transformation using Lambda Expression
        Image grayScaleImg = Images.transform(img, (color) -> {
            int gray = (color.getRedScale() + color.getGreenScale() + color.getBlueScale()) / 3;
            return new Color(gray, gray, gray);
        });
        
        System.out.println("Grayscale Image:");
        System.out.println(grayScaleImg);
        
        // Binarization transformation using Lambda Expression
        Image binarizedImg = Images.transform(img, (color) -> {
            int gray = (color.getRedScale() + color.getGreenScale() + color.getBlueScale()) / 3;
            if (gray <= 127) {
                return new Color(0, 0, 0); // Black
            } else {
                return new Color(255, 255, 255); // White
            }
        });
        
        System.out.println("Binarized Image:");
        System.out.println(binarizedImg);
    }
}
