package com.example.graphicslab;

import java.util.Objects;

public class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }


    // Перегрузка оператора сложение для точек
    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }

    // Перегрузка оператора для умножения на константу
    public Point multiply(double factor) {
        return new Point((int) Math.round(this.x * factor), (int) Math.round(this.y * factor));
    }

    // Перегрузка оператора вычитания для точек
    public Point subtract(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    // Перегрузка оператора умножения для точек (скалярное произведение)
    public int dotProduct(Point other) {
        return this.x * other.x + this.y * other.y;
    }

    static Point subtract(Point v1, Point v2) {
        return new Point(v1.x - v2.x, v1.y - v2.y);
    }

    public static int area(Point v1, Point v2) {
        return v1.x * v2.y - v1.y * v2.x;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
