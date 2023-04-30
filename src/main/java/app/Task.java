package app;

import io.github.humbleui.jwm.MouseButton;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Rect;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2d;
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
     * последняя СК окна
     */
    protected CoordinateSystem2i lastWindowCS;


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
    public void renderTask(Canvas canvas, CoordinateSystem2i windowCS) {
        canvas.save();
        // создаём перо
        try (var paint = new Paint()) {
            for (Point p : points) {
                paint.setColor(POINT_COLOR);
                Vector2i windowPos = windowCS.getCoords(p.pos.x, p.pos.y, ownCS);
                canvas.drawRect(Rect.makeXYWH(windowPos.x - POINT_SIZE, windowPos.y - POINT_SIZE, POINT_SIZE * 2, POINT_SIZE * 2), paint);
            }
            for (Circle c : circles) {
                paint.setColor(CIRCLE_COLOR);
                Vector2i center = windowCS.getCoords(c.pos.x, c.pos.y, ownCS);
//              Vector2d center = new Vector2d(pos.x + windowCS.getMin().x, pos.y + windowCS.getMin().y);
                double cf1 = (double) 900.0 / windowCS.getSize().x, cf2 = (double) 900.0 / windowCS.getSize().y;
                float radX = (float) (c.rad / cf1);// / windowCS.getSize().x * ownCS.getSize().x);
                // радиус вдоль оси y
                float radY = (float) (c.rad / cf2);// / windowCS.getSize().y * ownCS.getSize().y);
                // кол-во отсчётов цикла
                int loopCnt = 60;
                int loopCnt2 = loopCnt / 2;
                // создаём массив координат опорных точек
                float[] points = new float[loopCnt * 4];
                // запускаем цикл
                for (int i = 0; i < loopCnt; i++) {
                    // x координата первой точки
                    points[i * 4] = (float) (center.x + radX * Math.cos(Math.PI / loopCnt2 * i));
                    // y координата первой точки
                    points[i * 4 + 1] = (float) (center.y + radY * Math.sin(Math.PI / loopCnt2 * i));

                    // x координата второй точки
                    points[i * 4 + 2] = (float) (center.x + radX * Math.cos(Math.PI / loopCnt2 * (i + 1)));
                    // y координата второй точки
                    points[i * 4 + 3] = (float) (center.y + radY * Math.sin(Math.PI / loopCnt2 * (i + 1)));
                }
                // рисуем линии
                canvas.drawLines(points, paint);
            }
        }
        canvas.restore();
        // Сохраняем последнюю СК
        lastWindowCS = windowCS;
    }

    /**
     * Клик мыши по пространству задачи
     *
     * @param pos         положение мыши
     * @param mouseButton кнопка мыши
     */
    public void click(Vector2i pos, MouseButton mouseButton) {
        if (lastWindowCS == null) return;
        // получаем положение на экране
        Vector2d taskPos = ownCS.getCoords(pos, lastWindowCS);
        if (mouseButton.equals(MouseButton.PRIMARY)) {
            addPoint(taskPos);
            // если правая, то во второе
        } else if (mouseButton.equals(MouseButton.SECONDARY)) {
            addPoint(taskPos);
        }
    }

    /**
     * Добавить точку
     *
     * @param pos      положение
     */
    public void addPoint(Vector2d pos) {
        Point newPoint = new Point(pos);
        points.add(newPoint);
    }
}