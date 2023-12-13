package com.example.graphicslab;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.*;

import static com.example.graphicslab.MathUtils.bernstein;
import static com.example.graphicslab.Point.subtract;

public class DrawingUtils {

    private final GraphicsContext gc;
    private final Canvas canvas;

    public GraphicsContext getGc() {
        return gc;
    }

    public DrawingUtils(GraphicsContext gc, Canvas canvas) {
        this.gc = gc;
        this.canvas = canvas;
    }

    public Canvas getCanvas() {
        return canvas;
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

    public void drawLine(Point3D a, Point3D b, RGBPIXEL color) {
        drawLine(a.getX(), a.getY(), b.getX(), b.getY(), color);
    }
}
