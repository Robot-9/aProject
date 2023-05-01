import app.Point;
import app.Task;
import misc.CoordinateSystem2d;
import misc.Vector2d;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс тестирования
 */
public class UnitTest {

    /**
     * Тест
     *
     * @param points список точек
     */
    private static void test(ArrayList<Point> points) {
        Task task = new Task(new CoordinateSystem2d(10, 10, 20, 20), points);
        task.solve();
        // проверяем, что точек внутри окружностей хотя бы половина от всех точекж

        ArrayList<Point> p = task.getPoints();
        assert p.size() == points.size();
        int cnt = 0;
        for (int i = 0; i < p.size(); ++i) {
            if (p.get(i).isIn) {
                ++cnt;
            }
        }

        assert cnt * 2 >= p.size();
    }


    /**
     * Первый тест
     */
    @Test
    public void test1() {
        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(new Vector2d(1, 1)));
        points.add(new Point(new Vector2d(-1, 1)));
        points.add(new Point(new Vector2d(-1, 1)));
        points.add(new Point(new Vector2d(2, 1)));
        points.add(new Point(new Vector2d(1, 2)));
        points.add(new Point(new Vector2d(1, 2)));

        test(points);
    }

    /**
     * Второй тест
     */
    @Test
    public void test2() {
        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(new Vector2d(1, 1)));
        points.add(new Point(new Vector2d(2, 1)));
        points.add(new Point(new Vector2d(2, 2)));
        points.add(new Point(new Vector2d(1, 2)));

        test(points);
    }

    /**
     * Третий тест
     */
    @Test
    public void test3() {
        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(new Vector2d(1, 1)));
        points.add(new Point(new Vector2d(2, 1)));
        points.add(new Point(new Vector2d(2, 2)));
        points.add(new Point(new Vector2d(1, 2)));

        test(points);
    }

    /**
     * Четвертый тест
     */
    @Test
    public void test4() {
        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(new Vector2d(1, 1)));
        points.add(new Point(new Vector2d(2, 1)));
        points.add(new Point(new Vector2d(2, 2)));
        points.add(new Point(new Vector2d(1, 2)));
        points.add(new Point(new Vector2d(10, 2)));
        points.add(new Point(new Vector2d(1, 5)));

        test(points);
    }
}
