package com.example.graphicslab;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.graphicslab.MathUtils.bernstein;
import static com.example.graphicslab.MathUtils.calcFactorial;

public class DrawingUtils {

    private final GraphicsContext gc;

    public DrawingUtils(GraphicsContext gc) {
        this.gc = gc;
    }

    public void setPixel(int x, int y, RGBPIXEL color) {
        gc.setFill(Color.rgb(color.red, color.green, color.blue));
        gc.fillRect(x, y, 1, 1);
    }

    public void setPixel(Point p, RGBPIXEL color) {
        setPixel(p.x, p.y, color);
    }

    /**
     * Рисует отрезок на канве от точки (x1, y1) до точки (x2, y2).
     *
     * @param x1    Координата x начальной точки
     * @param y1    Координата y начальной точки
     * @param x2    Координата x конечной точки
     * @param y2    Координата y конечной точки
     * @param color Цвет отрезка
     */
    void drawLine(int x1, int y1, int x2, int y2, RGBPIXEL color) {
        int x = x1, y = y1; // Текущее значение точки, инициализируем координатами начальной точки
        int dx = x2 - x1, dy = y2 - y1;
        int ix, iy; // Величина приращений (-1, 0, 1) по координатам определяют направление движения
        int e; // Ошибка
        int i; // Счетчик цикла

        // Определение величины приращения по координатам, а также получение абсолютных значений dx, dy
        if (dx > 0) ix = 1;
        else if (dx < 0) {
            ix = -1;
            dx = -dx;
        } else ix = 0;

        if (dy > 0) iy = 1;
        else if (dy < 0) {
            iy = -1;
            dy = -dy;
        } else iy = 0;

        if (dx >= dy) {
            e = 2 * dy - dx; // Инициализация ошибки с поправкой на половину пиксела

            if (iy >= 0) { // Увеличиваем или не изменяем y
                // Основной цикл
                for (i = 0; i <= dx; i++) {
                    setPixel(x, y, color); // Выводим точку

                    if (e >= 0) {
                        // Ошибка стала неотрицательной
                        y += iy; // Изменяем y
                        e -= 2 * dx; // Уменьшаем ошибку
                    }

                    x += ix; // Всегда изменяем x
                    e += dy * 2; // И увеличиваем ошибку
                }
            } else { // y уменьшается
                for (i = 0; i <= dx; i++) {
                    setPixel(x, y, color);

                    if (e > 0) {
                        // Ошибка стала положительной. Условие изменилось с >= на >
                        y += iy;
                        e -= 2 * dx;
                    }

                    x += ix;
                    e += dy * 2;
                }
            }
        } else { // dy > dx
            e = 2 * dx - dy; // Инициализация ошибки с поправкой на половину пиксела

            if (ix >= 0) { // Увеличиваем или не изменяем x
                // Основной цикл
                for (i = 0; i <= dy; i++) {
                    setPixel(x, y, color);

                    if (e >= 0) {
                        // Ошибка стала неотрицательной
                        x += ix;
                        e -= 2 * dy;
                    }

                    y += iy;
                    e += dx * 2;
                }
            } else { // x уменьшается
                for (i = 0; i <= dy; i++) {
                    setPixel(x, y, color);

                    if (e > 0) {
                        // Ошибка стала положительной. Условие изменилось с >= на >
                        x += ix;
                        e -= 2 * dy;
                    }

                    y += iy;
                    e += dx * 2;
                }
            }
        }
    }


    public void drawLine(Point a, Point b, RGBPIXEL color) {
        drawLine(a.x, a.y, b.x, b.y, color);
    }

    List<Point> drawPoligon(List<Point> points, RGBPIXEL color) {
        for (int i = 0; i < points.size() - 1; i++) {
            drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, color);
        }
        drawLine(points.get(points.size() - 1).x, points.get(points.size() - 1).y, points.get(0).x, points.get(0).y, color);
        return points;
    }

    public static boolean isConvexPolygon(List<Point> points) {
        // true, если многоугольник выпуклый.

        int prevSign = 0;
        int currentSign = 0;

        int n = points.size();

        for (int i = 0; i < n; i++) {
            // Вычисляем знак текущего выражения
            currentSign = (points.get((i + 1) % n).x - points.get(i).x) * (points.get((i + 2) % n).y - points.get(i).y)
                    - (points.get((i + 1) % n).y - points.get(i).y) * (points.get((i + 2) % n).x - points.get(i).x);

            // Если знак текущего выражения не равен 0
            if (currentSign != 0) {
                // Если знак текущего выражения отличается от предыдущего => Многоугольник невыпуклый
                if (currentSign * prevSign < 0) {
                    return false;
                }
                // Сохраняем текущий знак
                prevSign = currentSign;
            }
        }
        return true;
    }

    public static boolean isIntersect(Point a, Point b, Point c, Point d) {
        // true, если отрезки пересекаются

        // Вычисляем знаки
        int sign1 = (c.x - a.x) * (b.y - a.y) - (b.x - a.x) * (c.y - a.y);
        int sign2 = (d.x - a.x) * (b.y - a.y) - (b.x - a.x) * (d.y - a.y);
        int sign3 = (a.x - c.x) * (d.y - c.y) - (d.x - c.x) * (a.y - c.y);
        int sign4 = (b.x - c.x) * (d.y - c.y) - (d.x - c.x) * (b.y - c.y);

        // Если знаки разные, то отрезки пересекаются
        return sign1 * sign2 < 0 && sign3 * sign4 < 0;
    }

    // Проверка на простоту многоугольника
    public static boolean isSimplePolygon(List<Point> points) {
        int n = points.size();

        for (int i = 0; i < n; i++) {
            for (int j = i + 2; j < n; j++) {
                if (i != j) {
                    // Если отрезки пересекаются, многоугольник не простой
                    if (
                            isIntersect(
                                    points.get(i),
                                    points.get((i + 1) % n),
                                    points.get(j),
                                    points.get((j + 1) % n)
                            )
                    ) {
                        return false;
                    }
                }
            }
        }
        // Многоугольник простой
        return true;
    }

    public static boolean isPointInPolygon(List<Point> points, Point dot, boolean flag) {
        // Если flag = true, то используется EO
        // Если flag = false, то используется NZW

        if (flag) {
            int n = points.size();
            // Количество пересечений
            int count = 0;

            for (int i = 0; i < n; i++) {
                // Если отрезок пересекается
                Point dot1 = points.get(i);
                Point dot2 = points.get((i + 1) % n);
                if (dot1.y == dot2.y)
                    continue;
                if (dot.y < Math.min(dot1.y, dot2.y))
                    continue;
                if (dot.y >= Math.max(dot1.y, dot2.y))
                    continue;
                double x = dot1.x + (double) ((dot.y - dot1.y) * (dot2.x - dot1.x)) / (dot2.y - dot1.y);
                if (x > dot.x)
                    count++;
            }

            // Если количество пересечений нечетное, то точка внутри многоугольника
            return count % 2 == 1;
        } else {
            // Находим максимальное значение x среди точек
            int maxX = points.stream().max(Comparator.comparingInt(a -> a.x)).get().x;

            int n = points.size();
            int count = 0;

            for (int i = 0; i < n; i++) {
                // Если отрезок пересекается
                Point dot1 = points.get(i);
                Point dot2 = points.get((i + 1) % n);
                if (dot1.y == dot2.y)
                    continue;
                if (dot.y < Math.min(dot1.y, dot2.y))
                    continue;
                if (dot.y >= Math.max(dot1.y, dot2.y))
                    continue;
                double x = dot1.x + (double) ((dot.y - dot1.y) * (dot2.x - dot1.x)) / (dot2.y - dot1.y);
                if (x > dot.x && dot1.y > dot2.y)
                    count--;
                if (x > dot.x && dot1.y < dot2.y)
                    count++;
            }

            // Если количество пересечений положительное, то точка внутри многоугольника
            return count > 0;
        }
    }


    public void colorPolygon(List<Point> points, RGBPIXEL color, boolean flag) {
        int minX = points.stream().min(Comparator.comparingInt(a -> a.x)).get().x;
        int maxX = points.stream().max(Comparator.comparingInt(a -> a.x)).get().x;
        int minY = points.stream().min(Comparator.comparingInt(a -> a.y)).get().y;
        int maxY = points.stream().max(Comparator.comparingInt(a -> a.y)).get().y;

        // Закрашиваем точки внутри области
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                // Если точка внутри многоугольника, то закрашиваем
                if (isPointInPolygon(points, new Point(x, y), flag)) {
                    setPixel(x, y, color);
                }
            }
        }
    }

    // Функция для отрисовки кривой Безье
    public void bezierCurve(List<Point> points, RGBPIXEL color) {
        int n = points.size();

        for (double t = 0; t <= 1; t += 0.001) {
            double x = 0;
            double y = 0;
            for (int i = 0; i < n; i++) {
                x += points.get(i).x * bernstein(n - 1, i, t);
                y += points.get(i).y * bernstein(n - 1, i, t);
            }
            setPixel((int) x, (int) y, color);
        }
    }

    // Функция для вычисления площади параллелограмма
    private static int area(Point v1, Point v2) {
        return v1.x * v2.y - v1.y * v2.x;
    }

    // Функция для инвертирования порядка точек в полигоне
    private static void reverse(List<Point> list) {
        Collections.reverse(list);
    }

    // Функция для нахождения точки пересечения двух отрезков
    private static Point intersection(Point a1, Point a2, Point b1, Point b2) {
        // Находим вектора отрезков
        Point a = a2.subtract(a1);
        Point b = b2.subtract(b1);

        // Проверяем, параллельны ли отрезки
        if (area(a, b) == 0) {
            // Отрезки параллельны или совпадают, возвращаем первую точку первого отрезка
            return a1;
        }

        // Вычисляем координаты точки пересечения
        double x = (double) ((a1.x * a2.y - a1.y * a2.x) * (b1.x - b2.x) - (a1.x - a2.x) * (b1.x * b2.y - b1.y * b2.x))
                / ((a1.x - a2.x) * (b1.y - b2.y) - (a1.y - a2.y) * (b1.x - b2.x));

        double y = (double) ((a1.x * a2.y - a1.y * a2.x) * (b1.y - b2.y) - (a1.y - a2.y) * (b1.x * b2.y - b1.y * b2.x))
                / ((a1.x - a2.x) * (b1.y - b2.y) - (a1.y - a2.y) * (b1.x - b2.x));

        return new Point((int) x, (int) y);
    }

    // Функция для алгоритма отсечения
    public void cuttingAlgorithmSB(List<Point> polygon, Point start, Point end, RGBPIXEL color) {
        double t1 = 0, t2 = 1;
        Point l = end.subtract(start);
        int n = polygon.size();

        if (area(polygon.get(1).subtract(polygon.get(0)), polygon.get(2).subtract(polygon.get(1))) < 0) {
            reverse(polygon);
        }

        for (int i = 0; i < n - 1; i++) {
            Point ans = intersection(polygon.get(i), polygon.get(i + 1), start, end);

            Point curVec = polygon.get(i + 1).subtract(polygon.get(i));
            Point norm = new Point(-curVec.y, curVec.x);

            double t = ((double) ans.x - (double) start.x) / l.x;
            if (t < 0 || t > 1) continue;

            if (l.dotProduct(norm) > 0) {
                t1 = Math.max(t1, t);
            } else {
                t2 = Math.min(t2, t);
            }
        }
        Point ans = intersection(polygon.get(n - 1), polygon.get(0), start, end);

        Point curVec = polygon.get(0).subtract(polygon.get(n - 1));
        Point norm = new Point(-curVec.y, curVec.x);
        double t = ((double) ans.x - (double) start.x) / l.x;
        if (t >= 0 && t <= 1) {
            if (l.dotProduct(norm) > 0) {
                t1 = Math.max(t1, t);
            } else {
                t2 = Math.min(t2, t);
            }
        }

        if (t2 < t1) return;

        Point p3 = new Point((int) Math.round(start.x + l.x * t1), (int) Math.round(start.y + l.y * t1));
        Point p4 = new Point((int) Math.round(start.x + l.x * t2), (int) Math.round(start.y + l.y * t2));

        drawLine(p3.x, p3.y, p4.x, p4.y, color);
    }
}
