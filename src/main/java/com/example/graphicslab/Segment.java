package com.example.graphicslab;

import javafx.util.Pair;

import java.util.Objects;

import static com.example.graphicslab.Point.area;

public class Segment {
    public Point start;
    public Point end;

    public Segment(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    // Метод, определяющий лежит ли точка v внутри отрезка segm
    public static boolean isInsideSegment(Segment segm, Point v) {
        return isBoxIntersects(segm.start.x, segm.end.x, v.x, v.x)
                && isBoxIntersects(segm.start.y, segm.end.y, v.y, v.y);
    }

    // Метод, проверяющий пересечение прямоугольников по осям X и Y
    public static boolean isBoxIntersects(int x1, int x2, int y1, int y2) {
        return Math.min(x1, x2) <= Math.max(y1, y2) && Math.min(y1, y2) <= Math.max(x1, x2);
    }

    // Метод, определяющий пересечение двух отрезков и тип этого пересечения
    public static Pair<Boolean, PlaceType> intersectSegment(Segment first, Segment second) {
        // Вызываем метод intersectSegment с вершинами отрезков
        return intersectSegment(first.start, first.end, second.start, second.end);
    }

    // Метод, определяющий пересечение двух отрезков и тип этого пересечения
    public static Pair<Boolean, PlaceType> intersectSegment(Point a, Point b, Point c, Point d) {
        // Вычисляем площадь параллелограмма, образованного векторами (b - a) и (d - c)
        int ab_cd = -area(b.subtract(a), d.subtract(c));

        // Если площадь равна 0, отрезки параллельны
        if (ab_cd == 0) {
            // Если вектора (d - c) и (c - a) тоже параллельны, то отрезки лежат на одной прямой
            if (area(d.subtract(c), c.subtract(a)) != 0)
                return new Pair<>(false, PlaceType.PARALLEL);

            // Проверяем, пересекаются ли проекции отрезков на оси X и Y
            return new Pair<>(isBoxIntersects(a.x, b.x, c.x, d.x)
                    && isBoxIntersects(a.y, b.y, c.y, d.y), PlaceType.COLLINEAR);
        }

        // Вычисляем параметры t1 и t2 для точек пересечения
        double t1 = (double) area(d.subtract(c), c.subtract(a)) / ab_cd;
        double t2 = (double) area(b.subtract(a), c.subtract(a)) / ab_cd;

        // Проверяем, лежат ли точки пересечения в пределах отрезков
        return new Pair<>(0 <= t1 && t1 <= 1 && 0 <= t2 && t2 <= 1, PlaceType.CROSS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return Objects.equals(start, segment.start) && Objects.equals(end, segment.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}