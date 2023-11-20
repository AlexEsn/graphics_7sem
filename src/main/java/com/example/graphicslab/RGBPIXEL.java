package com.example.graphicslab;

// Assuming RGBPIXEL is a class with red, green, and blue values
public class RGBPIXEL {
    public static final RGBPIXEL RED = new RGBPIXEL(255, 0, 0);
    public static final RGBPIXEL BLACK = new RGBPIXEL(0, 0, 0);

    public static final RGBPIXEL BLUE = new RGBPIXEL(0, 0, 255);

    public static final RGBPIXEL GREEN = new RGBPIXEL(0, 255, 0);

    int red;
    int green;
    int blue;

    public RGBPIXEL(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}
