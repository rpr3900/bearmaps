package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;


public class KDTreeTest {

    private static Random r = new Random(1000);

    private static KDTree buildLectureTree() {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
        return kd;
    }

    @Test
    public void nearest() {
        Point p1 = new Point(1, 7);
        Point p2 = new Point(1, 8);
        Point p3 = new Point(1, 9);
        Point p4 = new Point(1, 1);
        Point p5 = new Point(0, 1);
        Point p6 = new Point(2,  3);
        Point p7 = new Point(4,  2);
        Point p8 = new Point(4,  2);
        Point p9 = new Point(4,  5);
        Point p10 = new Point(0,  2);
        Point p11 = new Point(1,  5);
        Point p12 = new Point(4,  4);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12));
        Point actual = kd.nearest(1, 4);
        Point expected = new Point(1, 5);
        assertEquals(expected, actual);

        Point actual2 = kd.nearest(0, 0);
        Point expected2 = new Point(0,  1);
        assertEquals(expected2, actual2);

        Point actual3 = kd.nearest(10, 10);
        Point expected3 = new Point(4,  5);
        assertEquals(expected3, actual3);

        Point actual4 = kd.nearest(0, 2);
        Point expected4 = new Point(0,  2);
        assertEquals(expected4, actual4);
    }


    @Test
    public void testNearestDemoSlides() {
        KDTree kd = buildLectureTree();
        Point actual = kd.nearest(0, 7);
        Point expected = new Point(1, 5);
        assertEquals(expected, actual);
    }

    private Point randomPoint() {
        double x = r.nextDouble();
        double y = r.nextDouble();
        return new Point(x, y);
    }

    private List<Point> randomPoints(int N) {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            points.add(randomPoint());
        }
        return points;
    }

    @Test
    public void testWithRandomPoints() {
        int pointCount = 50000;
        int queryCount = 50000;
        List<Point> points = randomPoints(pointCount);
        NaivePointSet nps = new NaivePointSet(points);
        KDTree kd = new KDTree(points);

        List<Point> queries = randomPoints(queryCount);
        for (Point p : queries) {
            Point expected = nps.nearest(p.getX(), p.getY());
            Point actual = kd.nearest(p.getX(), p.getY());
            assertEquals(expected, actual);
        }
    }

    @Test
    public void naiveVsKdTree() {
        List<Point> randomPoints = randomPoints(100000);
        KDTree kd = new KDTree(randomPoints);
        NaivePointSet nps = new NaivePointSet(randomPoints);
        List<Point> queryPoints = randomPoints(10000);
        Stopwatch sw = new Stopwatch();
        for (Point p : queryPoints) {
            nps.nearest(p.getX(), p.getY());
        }
        double time = sw.elapsedTime();
        System.out.println("Naive:" + time);

        sw = new Stopwatch();
        for (Point p : queryPoints) {
            kd.nearest(p.getX(), p.getY());
        }
        time = sw.elapsedTime();
        System.out.println("KD tree:" + time);
    }
}
