package bearmaps;
import java.util.List;



public class KDTree implements PointSet {

    private static final Boolean HORIZONTAL = false;
    private static final Boolean VERTICAL = true;
    private Node root;

    private class Node {
        private Point p;
        private boolean orientation;
        private Node leftChild;  // Also refers to the down child
        private Node rightChild; // Also refers to the up child

        Node(Point givenP, boolean po) {
            p = givenP;
            orientation = po;
        }
    }

    public KDTree(List<Point> points) {
        for (Point p : points) {
            root = add(p, root, HORIZONTAL);
        }
    }

    private Node add(Point p, Node n, boolean orientation) {
        if (n == null) {
            return new Node(p, orientation);
        }

        int compare = comparePoints(p, n.p, orientation);
        if (compare < 0) {
            n.leftChild = add(p, n.leftChild, !orientation);
        } else if (compare >= 0) {
            n.rightChild = add(p, n.rightChild, !orientation);
        }
        return n;
    }

    private int comparePoints(Point a, Point b, boolean orientation) {
        if (orientation == HORIZONTAL) {
            return Double.compare(a.getX(), b.getX());
        } else {
            return Double.compare(a.getY(), b.getY());
        }
    }

    @Override
    public Point nearest(double x, double y) {
        Point p = new Point(x, y);
        return nearestHelper(root, p, root).p;
    }

    private Node nearestHelper(Node n, Point goal, Node best) {
        if (n == null) {
            return best;
        }
        double nearest = Point.distance(goal, best.p);
        double current = Point.distance(goal, n.p);
        if (current < nearest) {
            best = n;
        }
        double dX = n.p.getX();
        double goalX = goal.getX();
        double dY = n.p.getY();
        double goalY = goal.getY();
        double good = -1;
        Node badSide;
        Node goodSide;

        if (n.orientation == HORIZONTAL) {
            if (goalX < dX) {
                goodSide = n.leftChild;
                badSide = n.rightChild;
            } else {
                goodSide = n.rightChild;
                badSide = n.leftChild;
            }
        } else {
            if (goalY < dY) {
                goodSide = n.leftChild;
                badSide = n.rightChild;
            } else {
                goodSide = n.rightChild;
                badSide = n.leftChild;
            }
        }
        best = nearestHelper(goodSide, goal, best);
        if (n.orientation == HORIZONTAL) {
            if (badSide != null) {
                good = Math.pow(goalX - dX, 2);
            }
        }
        if (n.orientation == VERTICAL) {
            if (badSide != null) {
                good = Math.pow(goalY - dY, 2);
            }
        }
        if (Double.compare(good, nearest) < 0.000) {
            best = nearestHelper(badSide, goal, best);
        }
        return best;
    }
}
