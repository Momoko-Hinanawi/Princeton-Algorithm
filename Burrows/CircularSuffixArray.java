/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray
{
    private class Suffix
    {
        int offset = 0;

        char CharAt(int index)
        {
            return str.charAt((offset + index) % str.length());
        }

        Suffix(int offset)
        {
            this.offset = offset;
        }
    }

    private String str;
    private Suffix[] arr;

    // circular suffix array of s
    public CircularSuffixArray(String s)
    {
        if (s == null)
        {
            throw new IllegalArgumentException();
        }
        str = s;
        arr = new Suffix[str.length()];
        for (int i = 0; i != s.length(); ++i)
        {
            arr[i] = new Suffix(i);
        }
        Qsort(arr);
    }


    private void partition(Suffix[] a, int lo, int hi, int d)
    {
        if (d >= str.length()) return; //out of bound
        if (hi <= lo) return; //recurse ends
        int lt = lo, gt = hi;
        int v; //pivot
        v = a[lo].CharAt(d);
        int i = lo + 1;
        while (i <= gt) //three way partitioning
        {
            int t = a[i].CharAt(d);
            if (t < v)
            {
                exch(a, lt++, i++);
            } else if (t == v) ++i;
            else exch(a, i, gt--);
        }
        partition(a, lo, lt - 1, d);
        partition(a, lt, gt, d + 1);
        partition(a, gt + 1, hi, d);

    }

    private static void exch(Suffix[] a, int i, int j)
    {
        Suffix s = a[i];
        a[i] = a[j];
        a[j] = s;
    }

    private void Qsort(Suffix[] a) //Can be used on strings with different length
    {
        partition(a, 0, a.length - 1, 0);
    }

    // length of s
    public int length()
    {
        return str.length();
    }

    // returns index of ith sorted suffix
    public int index(int i)
    {
        return arr[i].offset;
    }

    // unit testing (required)
    public static void main(String[] args)
    {
        CircularSuffixArray suffixArray = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i != suffixArray.length(); ++i)
        {
            StdOut.println(suffixArray.index(i));
        }
    }

}
