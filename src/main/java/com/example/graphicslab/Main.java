package com.example.graphicslab;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {

    private static int WIDTH = 800;
    private static int HEIGHT = 800;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Графическая лаборатория");

        // Создаем холст
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        StackPane layout = new StackPane(canvas);
        Scene scene = new Scene(layout, WIDTH, HEIGHT);

        // Настраиваем контекст графики
        DrawingUtils drawingUtils = new DrawingUtils(canvas.getGraphicsContext2D());

        // -------------------------------------------------------------- Lab1 ----------------------------------------------
        // Пример 1: Рисуем линию
//        drawingUtils.drawLine(250, 250, 350, 350, new RGBPIXEL(255, 0, 0));

        // Пример 2: Рисуем многоугольник
//        List<Point> polygonPoints = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 0; i < 5; i++) {
//            polygonPoints.add(new Point(random.nextInt(500) + 50, random.nextInt(500) + 50));
//        }
//        drawingUtils.drawPoligon(polygonPoints, new RGBPIXEL(0, 0, 255));

        // Пример 3: Проверяем, является ли многоугольник выпуклым
//        boolean isConvex = DrawingUtils.isConvexPolygon(polygonPoints);
//        System.out.println("Является ли многоугольник выпуклым? " + isConvex);

        // Пример 4: Проверяем, пересекаются ли два отрезка
//        Point a = new Point(10, 10);
//        Point b = new Point(100, 100);
//        Point c = new Point(100, 10);
//        Point d = new Point(10, 100);
//        drawingUtils.drawLine(a, b, new RGBPIXEL(0, 255, 0));
//        drawingUtils.drawLine(c, d, new RGBPIXEL(0, 0, 255));
//        boolean doIntersect = DrawingUtils.isIntersect(a, b, c, d);
//        System.out.println("Пересекаются ли отрезки? " + doIntersect);

        // Пример 5: Проверяем, является ли многоугольник простым
//        boolean isSimple = DrawingUtils.isSimplePolygon(polygonPoints);
//        System.out.println("Является ли многоугольник простым? " + isSimple);

        // Пример 6: Проверяем, находится ли точка внутри многоугольника
//        Point testPoint = new Point(400, 400);
//        drawingUtils.setPixel(testPoint, new RGBPIXEL(0, 0, 0));
//        boolean isInside = DrawingUtils.isPointInPolygon(polygonPoints, testPoint, true);
//        System.out.println("Находится ли точка внутри многоугольника? " + isInside);

        // Пример 7: Закрашиваем внутренность многоугольника
//        drawingUtils.colorPolygon(polygonPoints, new RGBPIXEL(255, 255, 0), true);

        // a)
        // Оригинальный порядок: (0, 0) до (8, 3)
//        drawingUtils.drawLine(0, 0, 8, 3, RGBPIXEL.BLACK);
        //
        //// Обмен порядком: (8, 3) до (0, 0)
//        drawingUtils.drawLine(8, 3, 0, 0, RGBPIXEL.RED);
        // -------------------------------------------------------- Lab2 ------------------------------------------------------------
        // Пример 8: Рисуем кривую Безье 2-го порядка
//        drawingUtils.bezierCurve(List.of(new Point(50, 100), new Point(150, 200), new Point(250, 100)), RGBPIXEL.BLUE);

        //Пример 9: Рисуем кубическую кривую Безье
//        drawingUtils.bezierCurve(List.of(new Point(50, 100), new Point(150, 200), new Point(200, 50), new Point(300, 100)), RGBPIXEL.GREEN);

        //Пример 10: Рисуем кривую Безье 4-го порядка
//        drawingUtils.bezierCurve(List.of(new Point(50, 100), new Point(100, 200), new Point(200, 50), new Point(300, 200), new Point(400, 100)), RGBPIXEL.RED);

        // Пример 11: Отрисовываем отрезок с отсечением
//        List<Point> rectangle = Arrays.asList(new Point(200, 200), new Point(300, 350), new Point(500, 300), new Point(400, 150));

//        drawingUtils.drawPoligon(rectangle, RGBPIXEL.RED);

        // пересекает полигон
//        Point start_1 = new Point(50, 300);
//        Point end_1 = new Point(550, 280);

//        drawingUtils.drawLine(start_1, end_1, RGBPIXEL.GREEN);
//        drawingUtils.cuttingAlgorithmSB(
//                rectangle,
//                start_1,
//                end_1,
//                RGBPIXEL.BLUE
//        );

        // внутри полигона
//        Point start_2 = new Point(260, 190);
//        Point end_2 =  new Point(450, 250);

//        drawingUtils.drawLine(start_2, end_2, RGBPIXEL.GREEN);
//        drawingUtils.cuttingAlgorithmSB(
//                rectangle,
//                start_2,
//                end_2,
//                RGBPIXEL.BLUE
//        );

        // вне полигона
//        Point start_3 = new Point(50, 350);
//        Point end_3 = new Point(350, 400);

//        drawingUtils.drawLine(start_3, end_3, RGBPIXEL.GREEN);
//        drawingUtils.cuttingAlgorithmSB(
//                rectangle,
//                start_3,
//                end_3,
//                RGBPIXEL.BLUE
//        );

        // -------------------------------------------------------- BDZ ------------------------------------------------------------
        // Пример 12: Рисуем дугу окружности с использованием кривых Безье
        Point center = new Point(WIDTH / 2, HEIGHT / 2);
        int radius = 100;
        double startAngle = Math.toRadians(0);
        double endAngle = Math.toRadians(180);

        drawingUtils.drawCircleWithBezier(center, radius, startAngle, endAngle, RGBPIXEL.BLACK);

        // Пример использования findExternalContour
        List<Point> originalPolygon =  List.of(
                new Point(400, 400),
                new Point(600, 600),
                new Point(200, 500),
                new Point(600, 500),
                new Point(200, 600)
        );

        List<Point> externalContour = drawingUtils.findExternalContour(originalPolygon);

        drawingUtils.drawPoligon(originalPolygon, RGBPIXEL.RED);
        drawingUtils.drawPoligon(externalContour, RGBPIXEL.BLUE);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
