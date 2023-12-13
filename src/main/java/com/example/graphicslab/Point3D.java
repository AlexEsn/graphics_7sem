package com.example.graphicslab;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Point3D {
    public double x, y, z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(Point3D p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public int getX() {
        return (int) Math.round(x);
    }

    public int getY() {
        return (int) Math.round(y);
    }

    public int getZ() {
        return (int) Math.round(z);
    }

    public Point3D add(Point3D other) {
        return new Point3D(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Point3D add(double x, double y, double z) {
        return new Point3D(this.x + x, this.y + y, this.z + z);
    }

    public Point3D subtract(Point3D other) {
        return new Point3D(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Point3D multiply(double scalar) {
        return new Point3D(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public void multiplyThis(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    public Point3D divide(double scalar) {
        if (scalar == 0) {
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        return new Point3D(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    public double dotProduct(Point3D other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public static Point3D cross(Point3D a, Point3D b) {
        double x = a.y * b.z - a.z * b.y;
        double y = a.z * b.x - a.x * b.z;
        double z = a.x * b.y - a.y * b.x;

        return new Point3D(x, y, z);
    }
    public void rotate(double alpha, Point3D Point3D) {
        Point3D.normalizeThis();
        double cos_phi = cos(alpha);
        double sin_phi = sin(alpha);

        x = x * (cos_phi + Point3D.x * Point3D.x * (1 - cos_phi)) +
                y * (Point3D.x * Point3D.y * (1 - cos_phi) - Point3D.z * sin_phi) +
                z * (Point3D.x * Point3D.z * (1 - cos_phi) + Point3D.y * sin_phi);

        y = x * (Point3D.x * Point3D.y * (1 - cos_phi) + Point3D.z * sin_phi) +
                y * (cos_phi + Point3D.y * Point3D.y * (1 - cos_phi)) +
                z * (Point3D.y * Point3D.z * (1 - cos_phi) - Point3D.x * sin_phi);

        z = x * (Point3D.x * Point3D.z * (1 - cos_phi) - Point3D.y * sin_phi) +
                y * (Point3D.y * Point3D.z * (1 - cos_phi) + Point3D.x * sin_phi) +
                z * (cos_phi + Point3D.z * Point3D.z * (1 - cos_phi));
    }

    public void normalizeThis() {
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length != 0) {
            this.x /= length;
            this.y /= length;
            this.z /= length;
        } else {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }
    }
}
