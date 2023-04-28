package app;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;

import static app.Colors.CIRCLE_COLOR;


/**
 * Класс окружности
 */
public class Circle {
    /**
     * Координаты центра
     */
    public final Vector2d pos;
    /**
     * Радиус окружности
     */
    public final double rad;

    public Circle(Vector2d pos, double rad) {
        this.pos = pos;
        this.rad = rad;
    }

    /**
     * Получить положение
     *
     * @return положение
     */
    public Vector2d getPos() {
        return pos;
    }

    /**
     * Получить радиус
     *
     * @return радиус
     */
    public double getRad() {
        return rad;
    }

    void paint(Canvas canvas, CoordinateSystem2i windowCS, CoordinateSystem2d ownCS, Paint paint) {
        Vector2i center = windowCS.getCoords(pos.x, pos.y, ownCS);
//        Vector2d center = new Vector2d(pos.x + windowCS.getMin().x, pos.y + windowCS.getMin().y);
        double cf1 = (double) 900.0 / windowCS.getSize().x, cf2 = (double) 900.0 / windowCS.getSize().y;
        float radX = (float) (rad / cf1);// / windowCS.getSize().x * ownCS.getSize().x);
        // радиус вдоль оси y
        float radY = (float) (rad / cf2);// / windowCS.getSize().y * ownCS.getSize().y);
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
