package app;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Rect;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2i;

import java.util.ArrayList;

import static app.Colors.CIRCLE_COLOR;
import static app.Colors.POINT_COLOR;

/**
 * Класс задачи
 */
public class Task {
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
            ПОСТАНОВКА ЗАДАЧИ:
            На плоскости задано множество точек. Найти такие две окружности, что их центры находятся в точках заданного множества, внутри этих окружностей находятся хотя бы половина из всех точек заданного множества, и больший из двух радиусов минимален.    
            """;
    /**
     * Вещественная система координат задачи
     */
    private final CoordinateSystem2d ownCS;
    /**
     * Список точек
     */
    private final ArrayList<Point> points;
    /**
     * Список окружностей
     */
    private final ArrayList<Circle> circles;
    /**
     * Размер точки
     */
    private static final int POINT_SIZE = 3;


    /**
     * Задача
     *
     * @param ownCS  СК задачи
     * @param points массив точек
     */
    public Task(CoordinateSystem2d ownCS, ArrayList<Point> points, ArrayList<Circle> circles) {
        this.ownCS = ownCS;
        this.points = points;
        this.circles = circles;
    }

    /**
     * Рисование задачи
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        canvas.save();
        // создаём перо
        try (var paint = new Paint()) {
            for (Point p : points) {
                paint.setColor(POINT_COLOR);
                p.paint(canvas, windowCS, ownCS, paint);
            }
            for (Circle c : circles) {
                paint.setColor(CIRCLE_COLOR);
                c.paint(canvas, windowCS, ownCS, paint);
            }
        }
        canvas.restore();
    }
}