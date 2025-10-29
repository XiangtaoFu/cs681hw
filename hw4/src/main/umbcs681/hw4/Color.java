package umbcs681.hw4;

public class Color {
    private int red;
    private int green;
    private int blue;
    
    public Color(int red, int green, int blue) {
        this.red = Math.max(0, Math.min(255, red));
        this.green = Math.max(0, Math.min(255, green));
        this.blue = Math.max(0, Math.min(255, blue));
    }
    
    public int getRedScale() {
        return red;
    }
    
    public int getGreenScale() {
        return green;
    }
    
    public int getBlueScale() {
        return blue;
    }
    
    public void setRed(int red) {
        this.red = Math.max(0, Math.min(255, red));
    }
    
    public void setGreen(int green) {
        this.green = Math.max(0, Math.min(255, green));
    }
    
    public void setBlue(int blue) {
        this.blue = Math.max(0, Math.min(255, blue));
    }
    
    @Override
    public String toString() {
        return "Color(" + red + ", " + green + ", " + blue + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Color color = (Color) obj;
        return red == color.red && green == color.green && blue == color.blue;
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(red, green, blue);
    }
}
