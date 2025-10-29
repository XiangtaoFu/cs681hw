package umbcs681.hw4;

import java.util.ArrayList;
import java.util.List;

public class Image {
    private int height;
    private int width;
    private List<List<Color>> pixels;
    
    public Image(int height, int width) {
        this.height = height;
        this.width = width;
        this.pixels = new ArrayList<>();
        
        // Initialize with white pixels
        for (int i = 0; i < height; i++) {
            List<Color> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new Color(255, 255, 255)); // White pixel
            }
            pixels.add(row);
        }
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }
    
    public Color getColor(int x, int y) {
        if (x >= 0 && x < height && y >= 0 && y < width) {
            return pixels.get(x).get(y);
        }
        throw new IllegalArgumentException("Coordinates out of bounds: (" + x + ", " + y + ")");
    }
    
    public void setColor(int x, int y, Color color) {
        if (x >= 0 && x < height && y >= 0 && y < width) {
            pixels.get(x).set(y, color);
        } else {
            throw new IllegalArgumentException("Coordinates out of bounds: (" + x + ", " + y + ")");
        }
    }
    
    public List<List<Color>> getPixels() {
        return pixels;
    }
    
    public void setPixels(List<List<Color>> pixels) {
        this.pixels = pixels;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Image(").append(height).append("x").append(width).append(")\n");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(pixels.get(i).get(j)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
