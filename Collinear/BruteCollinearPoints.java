/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints
{
    private int number = 0;
    private Point[] A;
    private Point[] Aux;
    private LineSegment[] result;
    private LineSegment[] lineSegments = new LineSegment[128];

    public BruteCollinearPoints(Point[] points)   // finds all line segments containing 4 points
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
        A = new Point[points.length];
        for (int i = 0; i != points.length; ++i)
        {
            A[i] = points[i];
        }
        Aux = new Point[points.length];
        sort(0, A.length);
        for (int i = 1; i < A.length; ++i)
        {
            if (A[i].compareTo(A[i - 1]) == 0)
            {
                throw new java.lang.IllegalArgumentException();
            }
        }
        for (int i = 0; i < A.length; ++i)
        {
            for (int j = i + 1; j < A.length; ++j)
            {
                for (int k = j + 1; k < A.length; ++k)
                {
                    if (A[k].slopeTo(A[i]) == A[k].slopeTo(A[j]))
                        for (int l = k + 1; l < A.length; ++l)
                        {
                            if (A[l].slopeTo(A[i]) == A[k].slopeTo(A[i]))
                            {
                                lineSegments[number++] = new LineSegment(A[i], A[l]);
                            }
                        }
                }
            }
        }
        result = new LineSegment[number];
        for (int i = 0; i != result.length; ++i)
        {
            result[i] = lineSegments[i];
        }
    }

    private void sort(int lo, int hi)
    {
        if (hi - lo <= 1)
        {
            return;
        }
        int mid = (hi + lo) / 2;
        sort(lo, mid);
        sort(mid, hi);
        merge(lo, mid, hi);
    }

    private void merge(int lo, int mid, int hi)
    {

        for (int k = lo; k != hi; ++k)
        {
            Aux[k] = A[k];
        }
        int i = lo, j = mid;
        for (int k = lo; k != hi; ++k)
        {
            if (i >= mid) A[k] = Aux[j++];
            else if (j >= hi)
                A[k] = Aux[i++];
            else if (Aux[i].compareTo(Aux[j]) < 0) // ascentive
                A[k] = Aux[i++];
            else A[k] = Aux[j++];
        }
    }


    public int numberOfSegments()        // the number of line segments
    {
        return number;
    }

    public LineSegment[] segments()             // the line segments
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (int i = 0; i != collinear.numberOfSegments(); ++i)
        {
            StdOut.println(collinear.segments()[i]);
            collinear.segments()[i].draw();
        }
        StdDraw.show();
        StdOut.println(collinear.numberOfSegments());
    }

}
