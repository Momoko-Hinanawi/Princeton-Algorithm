/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

public class BurrowsWheeler
{

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    private static int R = 256;

    public static void transform()
    {
        String str = BinaryStdIn.readString();
        CircularSuffixArray a = new CircularSuffixArray(str);
        int first = 0;
        for (int i = 0; i != a.length(); ++i)
        {
            if (a.index(i) == 0)
            {
                first = i;
                break;
            }
        }
        BinaryStdOut.write(first);
        for (int i = 0; i != a.length(); ++i)
        {
            int pos = a.index(i) + a.length() - 1;
            BinaryStdOut.write(str.charAt(pos % str.length()), 8);
        }
        BinaryStdOut.close();
    }

    private static void transform(String str)
    {
        CircularSuffixArray a = new CircularSuffixArray(str);
        int first = 0;
        for (int i = 0; i != a.length(); ++i)
        {
            if (a.index(i) == 0)
            {
                first = i;
                break;
            }
        }
        StdOut.println(first);
        for (int i = 0; i != a.length(); ++i)
        {
            int pos = a.index(i) + a.length() - 1;
            StdOut.print(str.charAt(pos % str.length()));

        }
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform()
    {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();

        int[] next = new int[s.length()];
        char[] sorted = new char[s.length()];

        next[0] = first;
        int[] count = new int[R];
        for (int i = 0; i != s.length(); ++i)
            ++count[s.charAt(i)]; //key indexed counting
        for (int i = 1; i != count.length; ++i)
            count[i] += count[i - 1]; //accumulate position
        for (int i = count.length - 1; i > 0; --i)
            count[i] = count[i - 1];
        count[0] = 0;
        for (int i = 0; i != s.length(); ++i)
        {
            char c = s.charAt(i);
            next[count[c]] = i;
            sorted[count[c]] = c;
            ++count[c];
        }
        int position = first;
        for (int i = 0; i != s.length(); ++i)
        {
            BinaryStdOut.write(sorted[position], 8);
            position = next[position];
        }
        BinaryStdOut.close();
    }

    private static void inverseTransform(int first, String s)
    {
        int[] next = new int[s.length()];
        char[] sorted = new char[s.length()];

        next[0] = first;
        int[] count = new int[R];
        for (int i = 0; i != s.length(); ++i)
            ++count[s.charAt(i)]; //key indexed counting
        for (int i = 1; i != count.length; ++i)
            count[i] += count[i - 1]; //accumulate position
        for (int i = count.length - 1; i > 0; --i)
            count[i] = count[i - 1];
        count[0] = 0;
        for (int i = 0; i != s.length(); ++i)
        {
            char c = s.charAt(i);
            next[count[c]] = i;
            sorted[count[c]] = c;
            ++count[c];
        }
        int position = first;
        for (int i = 0; i != s.length(); ++i)
        {
            StdOut.print(sorted[position]);
            position = next[position];
        }
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args)
    {
        if (args[0].equals("+"))
        {
            transform();
        } else if (args[0].equals("-"))
        {
            inverseTransform();
        }
    }

}
