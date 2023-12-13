package com.example.graphicslab;

import java.util.List;

class Face {
    List<Point3D> points;
    Point3D center;
    Point3D n;

    Face(List<Point3D> points, Point3D center, Point3D n) {
        this.points = points;
        this.center = center;
        this.n = n;
    }

    void drawBounds(DrawingUtils drawingUtils, RGBPIXEL color) {
        drawingUtils.drawLine(points.get(0), points.get(1), color);
        drawingUtils.drawLine(points.get(1), points.get(2),color);
        drawingUtils.drawLine(points.get(2), points.get(3),color);
        drawingUtils.drawLine(points.get(3), points.get(0), color);
    }

    public List<Point3D> getPoints() {
        return points;
    }

    public void setCenter(Point3D center) {
        this.center = center;
    }

    public Point3D getCenter() {
        return center;
    }

    public void setN(Point3D n) {
        this.n = n;
    }

    public Point3D getN() {
        return n;
    }
}