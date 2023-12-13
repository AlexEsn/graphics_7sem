package com.example.graphicslab;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий параллелепипед в трехмерном пространстве.
 */
public class Parallelepiped {

    // Центр параллелепипеда
    Point3D center;

    // Список граней параллелепипеда
    List<Face> faces = new ArrayList<>();

    /**
     * Конструктор класса Parallelepiped.
     *
     * @param pMin Начальная точка (минимальные координаты) параллелепипеда
     * @param a    Длина параллелепипеда вдоль оси X
     * @param b    Ширина параллелепипеда вдоль оси Y
     * @param h    Высота параллелепипеда вдоль оси Z
     */
    public Parallelepiped(Point3D pMin, int a, int b, int h) {

        // Создание вершин параллелепипеда
        List<Point3D> lowVertices = createLowVertices(pMin, a, b);
        List<Point3D> highVertices = createHighVertices(lowVertices, h);

        // Создание и добавление граней параллелепипеда
        faces.add(new Face(lowVertices, new Point3D(0, 0, 0), new Point3D(0, 0, 1)));
        faces.add(new Face(highVertices, new Point3D(0, 0, 0), new Point3D(0, 0, -1)));

        List<Point3D> leftFaceVertices = createFaceVertices(lowVertices.get(0), lowVertices.get(1), highVertices.get(1), highVertices.get(0));
        List<Point3D> upFaceVertices = createFaceVertices(lowVertices.get(1), lowVertices.get(2), highVertices.get(2), highVertices.get(1));
        List<Point3D> rightFaceVertices = createFaceVertices(lowVertices.get(2), lowVertices.get(3), highVertices.get(3), highVertices.get(2));
        List<Point3D> downFaceVertices = createFaceVertices(lowVertices.get(3), lowVertices.get(0), highVertices.get(0), highVertices.get(3));

        faces.add(new Face(leftFaceVertices, new Point3D(0, 0, 0), new Point3D(1, 0, 0)));
        faces.add(new Face(upFaceVertices, new Point3D(0, 0, 0), new Point3D(0, -1, 0)));
        faces.add(new Face(rightFaceVertices, new Point3D(0, 0, 0), new Point3D(-1, 0, 0)));
        faces.add(new Face(downFaceVertices, new Point3D(0, 0, 0), new Point3D(0, 1, 0)));

        // Вычисление центра параллелепипеда
        center = calculateCenter(lowVertices, a, b, h);
        calculateFaceCenters();
        normalize();
    }

    /**
     * Создает список нижних вершин параллелепипеда.
     *
     * @param pMin Начальная точка (минимальные координаты) параллелепипеда
     * @param a    Длина параллелепипеда вдоль оси X
     * @param b    Ширина параллелепипеда вдоль оси Y
     * @return Список нижних вершин параллелепипеда
     */
    private List<Point3D> createLowVertices(Point3D pMin, int a, int b) {
        List<Point3D> lowVertices = new ArrayList<>();
        lowVertices.add(pMin);
        lowVertices.add(new Point3D(pMin.x, pMin.y + b, pMin.z));
        lowVertices.add(new Point3D(pMin.x + a, pMin.y + b, pMin.z));
        lowVertices.add(new Point3D(pMin.x + a, pMin.y, pMin.z));
        return lowVertices;
    }

    /**
     * Создает список верхних вершин параллелепипеда на основе списка нижних вершин и высоты.
     *
     * @param lowVertices Список нижних вершин параллелепипеда
     * @param h           Высота параллелепипеда вдоль оси Z
     * @return Список верхних вершин параллелепипеда
     */
    private List<Point3D> createHighVertices(List<Point3D> lowVertices, int h) {
        List<Point3D> highVertices = new ArrayList<>();
        for (Point3D vertex : lowVertices) {
            highVertices.add(new Point3D(vertex.x, vertex.y, vertex.z + h));
        }
        return highVertices;
    }

    /**
     * Создает список вершин грани параллелепипеда на основе четырех точек.
     *
     * @param p1 Первая точка
     * @param p2 Вторая точка
     * @param p3 Третья точка
     * @param p4 Четвертая точка
     * @return Список вершин грани параллелепипеда
     */
    private List<Point3D> createFaceVertices(Point3D p1, Point3D p2, Point3D p3, Point3D p4) {
        return List.of(p1, p2, p3, p4);
    }

    /**
     * Вычисляет центр параллелепипеда.
     *
     * @param lowVertices Список нижних вершин параллелепипеда
     * @param a           Длина параллелепипеда вдоль оси X
     * @param b           Ширина параллелепипеда вдоль оси Y
     * @param h           Высота параллелепипеда вдоль оси Z
     * @return Центр параллелепипеда
     */
    private Point3D calculateCenter(List<Point3D> lowVertices, int a, int b, int h) {
        return new Point3D(lowVertices.get(0).x + a / 2.0, lowVertices.get(0).y + b / 2.0, lowVertices.get(0).z + h / 2.0);
    }

    /**
     * Вычисляет центры граней параллелепипеда.
     */
    private void calculateFaceCenters() {
        for (Face face : faces) {
            Point3D faceCenter = new Point3D(0, 0, 0);
            for (Point3D vertex : face.getPoints()) {
                faceCenter = faceCenter.add(vertex);
            }
            face.setCenter(faceCenter.divide(4));
        }
    }

    /**
     * Нормализует грани параллелепипеда.
     */
    public void normalize() {
        for (Face face : faces) {
            face.setN(center.subtract(face.getCenter()));
        }
    }

    /**
     * Получает центр параллелепипеда.
     *
     * @return Центр параллелепипеда
     */
    public Point3D getCenter() {
        return new Point3D(center);
    }

    /**
     * Получает список граней параллелепипеда.
     *
     * @return Список граней параллелепипеда
     */
    public List<Face> getFaces() {
        return faces;
    }
}
