/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation
{
    private final int length; // length of the array
    private final int n;
    private final WeightedQuickUnionUF uf;
    private boolean[] grid; // if the site is open
    private int openSites;  // quantity of opened sites

    public Percolation(int p)
    {
        if (p <= 0)
        {
            throw new java.lang.IllegalArgumentException();
        }
        n = p;
        length = n * n;
        uf = new WeightedQuickUnionUF(length + 2);
        grid = new boolean[length + 2];
        for (int i = 0; i <= length + 1; ++i)
        {
            grid[i] = false;
        }
    } // create n-by-n grid, with all sites blocked

    private int convertToIndex(int x, int y)
    {
        return (x - 1) * n + y;
    }

    private boolean isLegal(int index)
    {
        return ((index > 0) && (index <= length));
    }

    private boolean isLegal(int x, int y)
    {
        return (x >= 1 && x <= n && y >= 1 && y <= n);
    }

    private int root(int p)
    {
        return uf.find(p);
    }

    private void merge(int p, int q)// merge p as a subnode of q
    {
        uf.union(p, q);
        // System.out.println(p + " and " + q + " are merged ");
    }

    public void open(int row, int col)    // open site (row, col) if it is not open already
    {
        int p = convertToIndex(row, col);
        if (!isLegal(row, col))
        {
            throw new java.lang.IllegalArgumentException();
        }
        if (grid[p])
        {
            return;
        }
        grid[p] = true;
        ++openSites;
        if (row == 1)
        {
            merge(p, 0);
        }
        if (row == n)
        {
            merge(p, length + 1);
        }
        if (isLegal(p + n) && grid[p + n])
        {
            merge(p, p + n);
        }
        if (isLegal(p - n) && grid[p - n])
        {
            merge(p, p - n);
        }
        if (isLegal(p + 1) && ((p % n) != 0) && grid[p + 1])
        {
            merge(p, p + 1);
        }
        if (isLegal(p - 1) && ((p % n) != 1) && grid[p - 1])
        {
            merge(p, p - 1);
        }

    }

    private boolean isConnected(int p, int q)
    {
        return root(p) == root(q);
    }

    public boolean isOpen(int row, int col)// is site (row, col) open?
    {
        int p = convertToIndex(row, col);
        if (!isLegal(row, col))
        {
            throw new java.lang.IllegalArgumentException();
        }
        return grid[p];
    }

    public boolean isFull(int row, int col)// is site (row, col) full?
    {
        int p = convertToIndex(row, col);
        if (!isLegal(row, col))
        {
            throw new java.lang.IllegalArgumentException();
        }
        return root(p) == root(0);
    }

    public int numberOfOpenSites()       // number of open sites?
    {
        return openSites;
    }

    public boolean percolates()              // does the system percolate?
    {
        return isConnected(0, length + 1);
    }

    public static void main(String[] args)
    {
        Percolation p = new Percolation(5);
        p.open(-1, 5);
    }
}
