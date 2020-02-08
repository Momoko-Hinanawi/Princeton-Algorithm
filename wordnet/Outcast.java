/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast
{
    private WordNet net;

    public Outcast(WordNet wordnet)         //TODO: constructor takes a WordNet object
    {
        net = wordnet;
    }

    public String outcast(String[] nouns)   //TODO: given an array of WordNet nouns, return an outcast
    {
        int len = nouns.length;
        int cache[][] = new int[len][len];
        for (int i = 0; i != nouns.length; ++i)
        {
            for (int j = 0; j != nouns.length; ++j)
            {
                if (i == j) continue;
                if (cache[i][j] != 0) continue;
                int distij = net.distance(nouns[i], nouns[j]);
                cache[i][j] = distij;
                cache[j][i] = distij;
            }
        }
        int outcastCandicate = 0;
        int currentMaxDistSum = -1;
        for (int i = 0; i != nouns.length; ++i)
        {
            int distSum = 0;
            for (int j = 0; j != nouns.length; ++j)
            {
                distSum += cache[i][j];
            }
            if (distSum > currentMaxDistSum)
            {
                outcastCandicate = i;
                currentMaxDistSum = distSum;
            }
        }
        return nouns[outcastCandicate];
    }

    public static void main(String[] args)
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++)
        {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
