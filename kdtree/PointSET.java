/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stack;
import java.util.TreeSet;
public class PointSET
{
    private TreeSet<Point2D> points = new TreeSet<>();

    public PointSET()                               // construct an empty set of points
    {

    }

    public boolean isEmpty()                      // is the set empty?
    {
        return points.size() == 0;
    }

    public int size()                         // number of points in the set
    {
        return points.size();
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        points.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        return points.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for (Point2D p : points)
        {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        Stack<Point2D> stack = new Stack<>();
        for (Point2D p : points)
        {
            if (p.x() <= rect.xmax() && p.x() >= rect.xmin() && p.y() <= rect.ymax() && p.y() >= rect.ymin())
            {
                stack.push(p);
            }
        }
        return stack;
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (isEmpty()) return null;
        Point2D ret = points.first();
        for (Point2D q : points)
        {
            if (p.distanceSquaredTo(q) < p.distanceSquaredTo(ret))
            {
                ret = q;
            }
        }
        return ret;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        PointSET pointSET = new PointSET();
        pointSET.insert(new Point2D(0, 0));
        pointSET.insert(new Point2D(114, 514));
        pointSET.insert(new Point2D(-113, -10));
        pointSET.insert(new Point2D(114, 515));
        pointSET.insert(new Point2D(100, 200));
        Iterable<Point2D> point2DS = pointSET.range(new RectHV(100, 100, 700, 700));
        for (Point2D p : point2DS)
        {
            StdOut.println(p);
        }
    }
}
