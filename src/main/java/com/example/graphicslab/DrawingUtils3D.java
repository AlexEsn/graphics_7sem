package com.example.graphicslab;

import java.util.ArrayList;
import java.util.List;

import static com.example.graphicslab.Point3D.cross;

/**
 * Утилитарный класс для рисования трехмерных объектов.
 */
public class DrawingUtils3D {

    // Экземпляр DrawingUtils для доступа к методам рисования 2D
    private final DrawingUtils drawingUtils;

    /**
     * Конструктор класса DrawingUtils3D.
     *
     * @param drawingUtils Экземпляр DrawingUtils, используемый для рисования 2D объектов
     */
    public DrawingUtils3D(DrawingUtils drawingUtils) {
        this.drawingUtils = drawingUtils;
    }

    /**
     * Поворачивает параллелепипед вокруг заданной точки на заданный угол.
     *
     * @param parallelepiped Параллелепипед, который нужно повернуть
     * @param alpha          Угол поворота в радианах
     * @param point          Точка, вокруг которой происходит поворот
     */
    public void rotate(Parallelepiped parallelepiped, double alpha, Point3D point) {
        for (Face face : parallelepiped.getFaces()) {
            for (Point3D p : face.getPoints()) {
                p.rotate(alpha, point);
            }
            face.getCenter().rotate(alpha, point);
        }
        parallelepiped.center.rotate(alpha, point);
        parallelepiped.normalize();
    }

    /**
     * Рисует параллелепипед без невидимых граней.
     *
     * @param parallelepiped Параллелепипед, который нужно нарисовать
     * @param color          Цвет граней параллелепипеда
     */
    public void drawWithoutVisibleEdges(Parallelepiped parallelepiped, RGBPIXEL color) {
        for (Face face : parallelepiped.getFaces()) {
            if (face.getN().z < 0) {
                face.drawBounds(drawingUtils, color);
            }
        }
    }

    /**
     * Рисует все грани параллелепипеда.
     *
     * @param parallelepiped Параллелепипед, который нужно нарисовать
     * @param color          Цвет граней параллелепипеда
     */
    public void drawAllBounds(Parallelepiped parallelepiped, RGBPIXEL color) {
        for (Face face : parallelepiped.getFaces()) {
            face.drawBounds(drawingUtils, color);
        }
    }

    /**
     * Создает экземпляр параллелепипеда с заданными параметрами.
     *
     * @param pMin Начальная точка (минимальные координаты) параллелепипеда
     * @param a    Длина параллелепипеда вдоль оси X
     * @param b    Ширина параллелепипеда вдоль оси Y
     * @param h    Высота параллелепипеда вдоль оси Z
     * @return Экземпляр параллелепипеда
     */
    public Parallelepiped createParallelepiped(Point3D pMin, int a, int b, int h) {
        return new Parallelepiped(pMin, a, b, h);
    }

    /**
     * Рисует одноточечную перспективную проекцию параллелепипеда.
     *
     * @param parallelepiped Параллелепипед, который нужно нарисовать
     * @param r              Коэффициент перспективы
     * @param color          Цвет граней параллелепипеда
     */
    public void drawOnePointProjection(Parallelepiped parallelepiped, double r, RGBPIXEL color) {
        Point3D centerProjection = onePointTransform(parallelepiped.getCenter(), r);
        for (Face face : parallelepiped.getFaces()) {
            Point3D faceCenter = onePointTransform(face.getCenter(), r);
            List<Point3D> points = new ArrayList<>();
            for (Point3D point : face.getPoints()) {
                points.add(onePointTransform(point, r));
            }

            Point3D n = cross(points.get(1).subtract(points.get(0)), points.get(2).subtract(points.get(1)));
            if (n.dotProduct(centerProjection.subtract(faceCenter)) < 0) {
                n = n.multiply(-1);
            }

            if (n.z < 0) {
                continue;
            }

            drawingUtils.drawLine(points.get(0), points.get(1), color);
            drawingUtils.drawLine(points.get(1), points.get(2), color);
            drawingUtils.drawLine(points.get(2), points.get(3), color);
            drawingUtils.drawLine(points.get(3), points.get(0), color);
        }
    }

    /**
     * Осуществляет преобразование точки в одноточечную перспективу.
     *
     * @param point Точка, которую нужно преобразовать
     * @param r     Коэффициент перспективы
     * @return Преобразованная точка
     */
    public Point3D onePointTransform(Point3D point, double r) {
        return point.multiply(1.0 / (r * point.z + 1));
    }
}
