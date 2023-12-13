package com.example.graphicslab;

import java.util.Collections;
import java.util.List;

import static com.example.graphicslab.MathUtils.bernstein;

public class BezierCurveUtils {
    
    private final DrawingUtils drawingUtils;

    public BezierCurveUtils(DrawingUtils drawingUtils) {
        this.drawingUtils = drawingUtils;
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
            drawingUtils.setPixel((int) x, (int) y, color);
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
                drawingUtils.setPixel(p1.x, p1.y, color);
                drawingUtils.setPixel(p2.x, p2.y, color);
                drawingUtils.setPixel(p3.x, p3.y, color);
                drawingUtils.setPixel(p4.x, p4.y, color);
            }
            // Обновление шага для следующей итерации
            step = beta - alfa;
        }
    }

}
