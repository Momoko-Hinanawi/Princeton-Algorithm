/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

import java.util.ArrayList;
import java.util.TreeMap;

public class WordNet
{
    private SAP sap;

    private class SynSet
    {
        private int id;
        private String[] words;
        private String name;

        public SynSet(String inputLine)
        {

            String[] strings = inputLine.split(",", 3);
            name = strings[1];
            id = Integer.parseInt(strings[0]);
            words = strings[1].split("\\s");

        }
    }

    //A word with corresponding sets
    private class Word
    {
        private ArrayList<Integer> Sets = new ArrayList<Integer>(); //List of sets that contains this word

        Word(int initialSet)
        {
            Sets.add(initialSet);
        }
    }

    private ArrayList<SynSet> synSetArrayList = new ArrayList<>();
    private TreeMap<String, Word> wordTreeMap = new TreeMap<>();

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {
        // TODO: check if The input to the constructor correspond to a rooted DAG.

        if (synsets == null || hypernyms == null) {throw new IllegalArgumentException();}
        //Input synsets and construct them
        In synsetStream = new In(synsets);
        while (!synsetStream.isEmpty())
        {
            SynSet synSet = new SynSet(synsetStream.readLine());
            synSetArrayList.add(synSet);
            for (String s : synSet.words)
            {
                if (!wordTreeMap.containsKey(s))
                {
                    wordTreeMap.put(s, new Word(synSet.id)); //add a new word in the treemap if not added previously
                } else
                {
                    wordTreeMap.get(s).Sets.add(synSet.id); // add the id to the sets
                }
            }
        }
        //Set hypernym vertices
        In hypernymStream = new In(hypernyms);
        Digraph digraph = new Digraph(synSetArrayList.size());
        while (!hypernymStream.isEmpty())
        {
            String[] inputLine = hypernymStream.readLine().split(",");
            int v = Integer.parseInt(inputLine[0]);
            for (int i = 1; i != inputLine.length; ++i)
            {
                digraph.addEdge(v, Integer.parseInt(inputLine[i]));
            }
        }
        Topological top = new Topological(digraph);
        if (!top.hasOrder()) throw new IllegalArgumentException();
        sap = new SAP(digraph);

    }

    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        return wordTreeMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        return wordTreeMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        if (isNoun(nounA) && isNoun(nounB))
            return sap.length(wordTreeMap.get(nounA).Sets, wordTreeMap.get(nounB).Sets);
        else throw new IllegalArgumentException();
    }

    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        if (isNoun(nounA) && isNoun(nounB))
        {
            int index = sap.ancestor(wordTreeMap.get(nounA).Sets, wordTreeMap.get(nounB).Sets);
            return synSetArrayList.get(index).name;
        } else throw new IllegalArgumentException();
    }

    //  do unit testing of this class
    public static void main(String[] args)
    {
        WordNet wordNet = new WordNet("synsets100-subgraph.txt", "hypernyms100-subgraph.txt");
        StdOut.println(wordNet.distance("calcium_ion", "growth_factor"));
        StdOut.println(wordNet.sap("calcium_ion", "growth_factor"));
    }
}
