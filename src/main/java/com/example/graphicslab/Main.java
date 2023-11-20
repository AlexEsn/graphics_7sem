package com.example.graphicslab;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Графическая лаборатория");

        // Создаем холст
        Canvas canvas = new Canvas(800, 800);
        StackPane layout = new StackPane(canvas);
        Scene scene = new Scene(layout, 800, 800);

        // Настраиваем контекст графики
        DrawingUtils drawingUtils = new DrawingUtils(canvas.getGraphicsContext2D());

        // Пример 1: Рисуем линию
        drawingUtils.drawLine(250, 250, 350, 350, new RGBPIXEL(255, 0, 0));

        // Пример 2: Рисуем многоугольник
        List<Point> polygonPoints = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            polygonPoints.add(new Point(random.nextInt(500) + 50, random.nextInt(500) + 50));
        }
        drawingUtils.drawPoligon(polygonPoints, new RGBPIXEL(0, 0, 255));

        // Пример 3: Проверяем, является ли многоугольник выпуклым
        boolean isConvex = DrawingUtils.isConvexPolygon(polygonPoints);
        System.out.println("Является ли многоугольник выпуклым? " + isConvex);

        // Пример 4: Проверяем, пересекаются ли два отрезка
        Point a = new Point(10, 10);
        Point b = new Point(100, 100);
        Point c = new Point(100, 10);
        Point d = new Point(10, 100);
        drawingUtils.drawLine(a, b, new RGBPIXEL(0, 255, 0));
        drawingUtils.drawLine(c, d, new RGBPIXEL(0, 0, 255));
        boolean doIntersect = DrawingUtils.isIntersect(a, b, c, d);
        System.out.println("Пересекаются ли отрезки? " + doIntersect);

        // Пример 5: Проверяем, является ли многоугольник простым
        boolean isSimple = DrawingUtils.isSimplePolygon(polygonPoints);
        System.out.println("Является ли многоугольник простым? " + isSimple);

        // Пример 6: Проверяем, находится ли точка внутри многоугольника
        Point testPoint = new Point(400, 400);
        drawingUtils.setPixel(testPoint, new RGBPIXEL(0, 0, 0));
        boolean isInside = DrawingUtils.isPointInPolygon(polygonPoints, testPoint, true);
        System.out.println("Находится ли точка внутри многоугольника? " + isInside);

        // Пример 7: Закрашиваем внутренность многоугольника
        drawingUtils.colorPolygon(polygonPoints, new RGBPIXEL(255, 255, 0), true);

        primaryStage.setScene(scene);
        primaryStage.show();

        // a)
        // Оригинальный порядок: (0, 0) до (8, 3)
        drawingUtils.drawLine(0, 0, 8, 3, RGBPIXEL.BLACK);
        //
        //// Обмен порядком: (8, 3) до (0, 0)
        drawingUtils.drawLine(8, 3, 0, 0, RGBPIXEL.RED);

    }
}
