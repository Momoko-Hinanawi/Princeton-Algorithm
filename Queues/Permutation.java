/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation
{
    public static void main(String[] args)
    {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        while (!StdIn.isEmpty())
        {
            String str = StdIn.readString();
            rq.enqueue(str);
        }
        for (int i = 0; i != k; ++i)
        {
            String str = rq.dequeue();
            StdOut.println(str);
        }
    }
}
