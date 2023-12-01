package com.example.graphicslab;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.*;

import static com.example.graphicslab.MathUtils.bernstein;
import static com.example.graphicslab.Point.subtract;

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

    public void setPixels(Point p, int n, RGBPIXEL color) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                setPixel(p.x + i, p.y + j, color);
            }
        }
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

        return ccw(a, c, d) != ccw(b, c, d) && ccw(a, b, c) != ccw(a, b, d);
    }

    // Функция для определения ориентации трех точек
    private static int ccw(Point a, Point b, Point c) {
        int area = (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y);

        if (area > 0) return 1;  // По часовой стрелке
        else if (area < 0) return -1;  // Против часовой стрелки
        else return 0;  // Коллинеарны
    }

    public static boolean isIntersect(Segment a, Segment b) {
        return isIntersect(a.start, a.end, b.start, b.end);
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

    // Функция для отрисовки дуги окружности с использованием кривых Безье
    public void drawCircleWithBezier(Point center, int radius, double alfa, double beta, RGBPIXEL color) {
        // Шаг для построения дуги
        double step = Math.PI / 4;

        // Пока не достигнут конечный угол
        while (alfa < beta) {
            // Расчет радиуса для контрольных точек
            double R = radius / Math.sin(Math.PI / 2 - step / 2);

            // Коэффициент для расчета контрольных точек
            double F = 4.0 / 3 / (1 + 1 / Math.cos(step / 4));

            // Пока не достигнут конечный угол с учетом погрешности
            while (alfa + step <= beta + 1e-2) {
                // Вычисление точек для построения кривой Безье
                Point p1 = center.add(new Point((int) (radius * Math.cos(alfa)), (int) (radius * Math.sin(alfa))));
                Point p4 = center.add(new Point((int) (radius * Math.cos(alfa + step)), (int) (radius * Math.sin(alfa + step))));
                Point pt = center.add(new Point((int) (R * Math.cos(alfa + step / 2)), (int) (R * Math.sin(alfa + step / 2))));
                Point p2 = p1.add(pt.subtract(p1).multiply(F));
                Point p3 = p4.add(pt.subtract(p4).multiply(F));

                // Отрисовка кривой Безье
                bezierCurve(List.of(p1, p2, p3, p4), color);
                alfa += step;

                // Отметка пикселей контрольных точек на дуге
                setPixel(p1.x, p1.y, color);
                setPixel(p2.x, p2.y, color);
                setPixel(p3.x, p3.y, color);
                setPixel(p4.x, p4.y, color);
            }
            // Обновление шага для следующей итерации
            step = beta - alfa;
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

    public class Tuple<T1, T2, T3> {
        public T1 first;
        public T2 second;
        public T3 third;

        public Tuple(T1 first, T2 second, T3 third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }

    public Tuple<Double, Double, PlaceType> intersectionPoint(Point a, Point b, Point c, Point d) {
        int ab_cd = -area(subtract(b, a), subtract(d, c));

        if (ab_cd == 0) {  // Parallel lines
            if (area(subtract(d, c), subtract(c, a)) != 0)
                return new Tuple<>(0.0, 0.0, PlaceType.PARALLEL);

            // Collinear lines
            if (a.x == c.x) {
                double from_1 = Math.min(a.y, b.y);
                double to_1 = Math.max(a.y, b.y);
                double from_2 = Math.min(c.y, d.y);
                double to_2 = Math.max(c.y, d.y);
                return new Tuple<>((from_2 - from_1) / (to_1 - from_1), 0.0, PlaceType.COLLINEAR);
            } else {
                double from_1 = Math.min(a.x, b.x);
                double to_1 = Math.max(a.x, b.x);
                double from_2 = Math.min(c.x, d.x);
                double to_2 = Math.max(c.x, d.x);
                return new Tuple<>((from_2 - from_1) / (to_2 - from_1), 0.0, PlaceType.COLLINEAR);
            }
        }

        double t1 = (double) area(subtract(d, c), subtract(c, a)) / ab_cd;
        double t2 = (double) area(subtract(b, a), subtract(c, a)) / ab_cd;

        return new Tuple<>(t1, t2, PlaceType.CROSS);
    }

    private Tuple<Double, Double, PlaceType> intersectionPoint(Segment a, Segment b) {
        return intersectionPoint(a.start, a.end, b.start, b.end);
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

    public List<Point> findExternalContour(List<Point> inputPolygon) {
        List<Point> externalContour = new ArrayList<>();

        List<Segment> segments = convertToSegments(inputPolygon);
        List<Segment> resultSegments = findExternalContourSegment(segments);

        // Преобразуем отрезки обратно в точки
        for (Segment segment : resultSegments) {
            externalContour.add(segment.start);
        }

        return externalContour;
    }

    private List<Segment> convertToSegments(List<Point> polygon) {
        List<Segment> segments = new ArrayList<>();

        for (int i = 0; i < polygon.size() - 1; i++) {
            segments.add(new Segment(polygon.get(i), polygon.get(i + 1)));
        }
        segments.add(new Segment(polygon.get(polygon.size() - 1), polygon.get(0)));

        return segments;
    }

    // Метод для нахождения внешнего контура по отрезкам
    public List<Segment> findExternalContourSegment(List<Segment> segments) {
        // Находим начальный индекс сегмента с самой левой точкой
        int startIndex = 0;
        for (int i = 1; i < segments.size(); ++i)
            if (segments.get(i).start.x < segments.get(startIndex).start.x)
                startIndex = i;

        List<Point> contourPoints = new ArrayList<>();
        Map<Point, List<Integer>> pointIndexMap = new HashMap<>();

        // Построение списка точек контура и карты индексов точек
        for (int k = startIndex; k < segments.size() + startIndex; ++k) {
            int index = k;
            if (index >= segments.size())
                index -= segments.size();

            contourPoints.add(segments.get(index).start);
            pointIndexMap.computeIfAbsent(segments.get(index).start, key -> new ArrayList<>()).add(contourPoints.size() - 1);

            List<Pair<Double, Point>> intersectionPoints = new ArrayList<>();
            for (int j = 0; j < segments.size(); ++j) {
                if (Math.abs(index - j) <= 1 || Math.abs(index - j) == segments.size() - 1)
                    continue;

                if (isIntersect(segments.get(index), segments.get(j))) {
                    Tuple<Double, Double, PlaceType> intersectionResult = intersectionPoint(segments.get(index), segments.get(j));
                    double t1 = intersectionResult.first;
                    intersectionPoints.add(new Pair<>(t1, segments.get(index).start.add((segments.get(index).end.subtract(segments.get(index).start)).multiply(t1))));
                }
            }

            // Если есть точки пересечения, добавляем их в список точек контура
            if (!intersectionPoints.isEmpty()) {
                intersectionPoints.sort(Comparator.comparingDouble(Pair::getKey));
                for (Pair<Double, Point> pair : intersectionPoints) {
                    contourPoints.add(pair.getValue());
                    pointIndexMap.computeIfAbsent(pair.getValue(), key -> new ArrayList<>()).add(contourPoints.size() - 1);
                }
            }
        }

        List<Segment> contourSegments = new ArrayList<>();
        contourSegments.add(new Segment(contourPoints.get(0), contourPoints.get(1)));
        System.out.println("add segment: [" + contourSegments.get(0).start + ", " + contourSegments.get(0).end + "]");
        pointIndexMap.remove(contourPoints.get(0));
        int count = 2;
        int currentPointIndex = 1;

        // Построение сегментов контура
        while (!pointIndexMap.isEmpty() && count <= contourPoints.size()) {
            if (currentPointIndex >= contourPoints.size())
                currentPointIndex -= contourPoints.size();

            // Если текущая точка не содержится в карте, ищем первую доступную точку
            if (!pointIndexMap.containsKey(contourPoints.get(currentPointIndex))) {
                for (int i = 0; i < contourPoints.size(); ++i) {
                    if (!pointIndexMap.containsKey(contourPoints.get(i)))
                        continue;
                    if (isInsideEvenOddRule(contourSegments, (contourPoints.get(i).add(contourPoints.get(i + 1))).multiply((double) 1 / 2))) {
                        pointIndexMap.remove(contourPoints.get(i));
                        continue;
                    }
                    currentPointIndex = i;
                    break;
                }
            }

            // Если у текущей точки только один индекс, удаляем его и строим новый сегмент
            if (pointIndexMap.get(contourPoints.get(currentPointIndex)).size() <= 1) {
                pointIndexMap.remove(contourPoints.get(currentPointIndex));
                int nextIndex = (currentPointIndex + 1) % contourPoints.size();
                contourSegments.add(new Segment(contourPoints.get(currentPointIndex), contourPoints.get(nextIndex)));
                count++;
                currentPointIndex++;
                continue;
            }

            // Если у текущей точки несколько индексов, выбираем следующий индекс и строим новый сегмент
            List<Integer> indices = pointIndexMap.get(contourPoints.get(currentPointIndex));
            int finalCurrentPointIndex = currentPointIndex;
            currentPointIndex = indices.stream()
                    .filter(j -> j != finalCurrentPointIndex)
                    .findFirst().get();
            int nextIndex = (currentPointIndex + 1) % contourPoints.size();
            contourSegments.add(new Segment(contourPoints.get(currentPointIndex), contourPoints.get(nextIndex)));
            count++;
            currentPointIndex++;
        }

        return contourSegments;
    }

    // Метод, реализующий проверку вхождения точки внутрь многоугольника по правилу четности
    public boolean isInsideEvenOddRule(List<Segment> segments, Point v) {
        if (segments.size() <= 2)
            return false;

        Point end = new Point(0, v.y - 10);
        Segment line = new Segment(v, end);
        int count = 0;

        for (Segment segm : segments) {
            Pair<Boolean, PlaceType> check = intersectSegment(segm, line);
            if (check.getKey()) {
                if (check.getValue() == PlaceType.CROSS || isInsideSegment(segm, v))
                    count++;
            }
        }

        return count % 2 != 0;
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

    // Метод, проверяющий пересечение прямоугольников по осям X и Y
    private static boolean isBoxIntersects(int x1, int x2, int y1, int y2) {
        return Math.min(x1, x2) <= Math.max(y1, y2) && Math.min(y1, y2) <= Math.max(x1, x2);
    }


    // Метод, определяющий лежит ли точка v внутри отрезка segm
    private static boolean isInsideSegment(Segment segm, Point v) {
        return isBoxIntersects(segm.start.x, segm.end.x, v.x, v.x)
                && isBoxIntersects(segm.start.y, segm.end.y, v.y, v.y);
    }

    // Перечисление, представляющее типы расположения точек и отрезков
    public enum PlaceType {
        PARALLEL,   // Параллельны
        COLLINEAR,  // Лежат на одной прямой
        CROSS       // Пересекаются
    }

}
