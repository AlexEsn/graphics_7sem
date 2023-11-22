package com.example.graphicslab;
public class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Перегрузка оператора вычитания для точек
    public Point subtract(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    // Перегрузка оператора умножения для точек (скалярное произведение)
    public int dotProduct(Point other) {
        return this.x * other.x + this.y * other.y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
