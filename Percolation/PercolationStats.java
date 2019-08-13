/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats
{
    private final int size; // size of the grid
    private final int length; // =n*n
    private final int trialTimes; // trial time
    private Percolation percolation; // percolation object
    private int[] results;
    private final double mean;
    private final double stdDev;

    public PercolationStats(int n,
                            int trials)    // perform trials independent experiments on an n-by-n grid
    {
        if (n <= 0 || trials <= 0)
        {
            throw new java.lang.IllegalArgumentException();
        }
        size = n;
        length = n * n;
        trialTimes = trials;
        results = new int[trials];
        roll();
        mean = StdStats.mean(results) / length;
        stdDev = StdStats.stddev(results) / length;

    }

    private int trial()
    {
        percolation = new Percolation(size);
        while (true)
        {
            int x = StdRandom.uniform(1, size + 1);
            int y = StdRandom.uniform(1, size + 1);
            percolation.open(x, y);
            if (percolation.percolates())
            {
                return percolation.numberOfOpenSites();
            }
        }
    }


    private void roll()
    {
        for (int i = 0; i != trialTimes; ++i)
        {
            results[i] = trial();
        }
    }

    public double mean()                        // sample mean of percolation threshold
    {
        return mean;
    }

    public double stddev()                      // sample standard deviation of percolation threshold
    {
        return stdDev;
    }

    public double confidenceLo()               // low  endpoint of 95% confidence interval
    {
        return mean() - stddev() * 1.96 / Math.sqrt(trialTimes);
    }

    public double confidenceHi()                  // high endpoint of 95% confidence interval
    {
        return mean() + stddev() * 1.96 / Math.sqrt(trialTimes);
    }

    public static void main(String[] args)        // test client (described below)
    {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats p = new PercolationStats(n, t);
        StdOut.println("mean                    = " + p.mean());
        StdOut.println("stddev                  = " + p.stddev());
        StdOut.println(
                "95% confidence interval = [" + p.confidenceLo() + ", " + p.confidenceHi() + "]");
    }
}
