package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.humbleui.jwm.MouseButton;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Rect;
import lombok.Getter;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;
import panels.PanelLog;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static app.Colors.CIRCLE_COLOR;
import static app.Colors.POINT_COLOR;

/**
 * Класс задачи
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class Task {
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
             ПОСТАНОВКА ЗАДАЧИ:
            На плоскости задано множество точек.\040
            Найти такие две окружности, что их центры\040
            находятся в точках заданного множества, внутри\040
            этих окружностей находятся хотя бы половина из\040
            всех точек заданного множества, и больший из\040
            двух радиусов минимален.\040\040\040
             """;
    /**
     * Вещественная система координат задачи
     */
    @Getter
    private final CoordinateSystem2d ownCS;
    /**
     * Список точек
     */
    @Getter
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
    public Task( CoordinateSystem2d ownCS,  ArrayList<Point> points, ArrayList<Circle> circles) {
        this.ownCS = ownCS;
        this.points = points;
        this.circles = circles;
    }

    /**
     * Задача
     *
     * @param ownCS  СК задачи
     * @param points массив точек
     */
    @JsonCreator
    public Task(@JsonProperty("ownCS") CoordinateSystem2d ownCS, @JsonProperty("points") ArrayList<Point> points) {
        this.ownCS = ownCS;
        this.points = points;
        this.circles = new ArrayList<>();
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
                CoordinateSystem2d windowCSd = new CoordinateSystem2d(windowCS);
                Vector2d center = windowCSd.getCoords(c.pos.x, c.pos.y, ownCS);
//              Vector2d center = new Vector2d(pos.x + windowCS.getMin().x, pos.y + windowCS.getMin().y);
//                double cf1 = (double) 900.0 / windowCS.getSize().x, cf2 = (double) 900.0 / windowCS.getSize().y;
                Vector2d rd = windowCSd.getLens(c.rad, c.rad, ownCS);
                double cf1 = rd.x, cf2 = rd.y;
                float radX = (float) (cf1);// / windowCS.getSize().x * ownCS.getSize().x);
                // радиус вдоль оси y
                float radY = (float) (cf2);// / windowCS.getSize().y * ownCS.getSize().y);
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
     * @param pos положение
     */
    public void addPoint(Vector2d pos) {
        Point newPoint = new Point(pos);
        points.add(newPoint);
        PanelLog.info("point add: " + newPoint);
    }

    /**
     * Добавить случайные точки
     *
     * @param cnt кол-во случайных точек
     */
    public void addRandomPoints(int cnt) {
        // если создавать точки с полностью случайными координатами,
        // то вероятность того, что они совпадут крайне мала
        // поэтому нужно создать вспомогательную малую целочисленную ОСК
        // для получения случайной точки мы будем запрашивать случайную
        // координату этой решётки (их всего 30х30=900).
        // после нам останется только перевести координаты на решётке
        // в координаты СК задачи
        CoordinateSystem2i addGrid = new CoordinateSystem2i(30, 30);

        // повторяем заданное количество раз
        for (int i = 0; i < cnt; i++) {
            // получаем случайные координаты на решётке
            Vector2i gridPos = addGrid.getRandomCoords();
            // получаем координаты в СК задачи
            Vector2d pos = ownCS.getCoords(gridPos, addGrid);
            addPoint(pos);
        }
    }

    /**
     * Очистить задачу
     */
    public void clear() {
        points.clear();
        circles.clear();
        PanelLog.info("clear");
    }

    /**
     * Решить задачу
     */
    public void solve() {
        circles.clear();
        double rl = 0, rr = 20000;
        int ans1 = 0, ans2 = 0;
        double rad = 0;
        for (int i = 0; i < 200; ++i) {
            double m = (rl + rr) / 2.0;
            int maxcnt = 0;
            int s1 = 0, s2 = 0;
            for (int j = 0; j < points.size(); ++j) {
                for (int k = j; k < points.size(); ++k) {
                    int cnt = 0;
                    for (Point p : points) {
                        if (p.dist(points.get(j).pos) <= m || p.dist(points.get(k).pos) <= m) {
                            ++cnt;
                        }
                    }
                    if (cnt > maxcnt) {
                        maxcnt = cnt;
                        s1 = j;
                        s2 = k;
                    }
                }
            }
            if (maxcnt * 2 >= points.size()) {
                ans1 = s1;
                ans2 = s2;
                rad = m;
                rr = m;
            } else {
                rl = m;
            }
        }
        Circle a1 = new Circle(points.get(ans1).pos, rad);
        circles.add(a1);
        a1 = new Circle(points.get(ans2).pos, rad);
        circles.add(a1);
        PanelLog.success("Задача решена + \n + Circles: " + circles.toString() + "\n" + circles.toString());
    }

    /**
     * Отмена решения задачи
     */
    public void cancel() {

    }
}