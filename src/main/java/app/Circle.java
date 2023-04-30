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


}
