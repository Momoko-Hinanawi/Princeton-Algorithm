/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints
{
    private Point[] pts;
    private Point[] traverseOrder;
    private LineSegment[] lineSegments = new LineSegment[32767];
    private LineSegment[] result;
    private int numberOfSegments = 0;

    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null)
        {
            throw new java.lang.IllegalArgumentException();
        }
        for (Point p : points)
        {
            if (p == null)
                throw new java.lang.IllegalArgumentException();

        }
        // fuckin OJ doctrine: CANNOT modify the arguments
        pts = new Point[points.length];
        for (int i = 0; i != pts.length; ++i)
        {
            pts[i] = points[i];
        }
        traverseOrder = new Point[points.length];

        for (int i = 0; i != pts.length; ++i)
        {
            traverseOrder[i] = pts[i];
        }

        Arrays.sort(traverseOrder, Point::compareTo);
        for (int i = 1; i < traverseOrder.length; ++i)
        {
            if (traverseOrder[i].compareTo(traverseOrder[i - 1]) == 0)
            {
                throw new java.lang.IllegalArgumentException();
            }
        }
        for (int i = 0; i != traverseOrder.length; ++i)
        {
            for (int j = i; j != pts.length; ++j)
            {
                pts[j] = traverseOrder[j];
            }
            Point origin = traverseOrder[i];
            // StdOut.println(origin.toString());
            Arrays.sort(pts, i, pts.length, origin.slopeOrder());
            // StdOut.println();
            int counter = 0;
            for (int j = i + 1; j != pts.length; ++j)
            {

                if (origin.slopeTo(pts[j]) == origin.slopeTo(pts[j - 1])) ++counter;
                else
                {
                    counter = 0;
                }

                // StdOut.println(pts[0].slopeTo(pts[j]) + " " + counter);
                if (counter == 2)
                {
                    Point[] temp = {origin, pts[j], pts[j - 1], pts[j - 2]};
                    Arrays.sort(temp, Point::compareTo);
                    lineSegments[numberOfSegments++] = new LineSegment(temp[0], temp[3]);
                    counter = 0;
                }
            }
        }
        result = new LineSegment[numberOfSegments];
        for (int i = 0; i != numberOfSegments; ++i)
        {
            result[i] = lineSegments[i];
        }
    }

    public int numberOfSegments()        // the number of line segments
    {
        return numberOfSegments;
    }

    public LineSegment[] segments()                // the line segments
    {
        return result;
    }

    public static void main(String[] args)
    {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++)
        {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points)
        {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (int i = 0; i != collinear.numberOfSegments(); ++i)
        {
            StdOut.println(collinear.segments()[i]);
            collinear.segments()[i].draw();
        }
        StdDraw.show();

    }
}
