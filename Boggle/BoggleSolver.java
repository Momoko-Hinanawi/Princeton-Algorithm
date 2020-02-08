/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver
{
    // Initializes the data structure using the given array of Strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    // In move sequences Qu is represented by 'Q', but in trie 'Qu' is represented by two chars
    private class RWayTries
    {
        private Integer R = 27;//extended ascii
        private Node root = new Node(); //root is an empty node

        public void Put(String key)
        {
            if (key == null)
            {
                throw new IllegalArgumentException();
            }
            root = PutAt(key, root, 0);
        }

        private Node PutAt(String key, Node node, Integer position) //node:current node; position:next position in the String
        {
            if (node == null) node = new Node();
            if (position == key.length())
            {
                node.Key = key;
                return node;
            }
            char c = key.charAt(position);
            node.next[c - 'A'] = PutAt(key, node.next[key.charAt(position) - 'A'], position + 1);
            return node;
        }

        private class Node
        {
            private Node[] next = new Node[R];
            public String Key = null;

            public Node Next(char c)
            {
                return next[c - 'A'];
            }
        }


        private void Collect(String prefix, Queue<String> queue, Node node)
        {
            if (node == null) return;
            if (node.Key != null) queue.enqueue(prefix);
            for (char c = 0; c != R; ++c)
            {
                Collect(prefix + c, queue, node.Next(c));
            }
        }

        public Iterable<String> Prefix(String prefix)
        {
            Queue<String> s = new Queue<String>();
            Node start = root;
            for (int i = 0; i != prefix.length(); ++i)
            {
                start = start.Next(prefix.charAt(i));
            }
            Collect(prefix, s, start);
            return s;
        }

        public boolean Get(String key)
        {
            return Getv(key, root, 0);
        }

        private boolean Getv(String key, Node node, int position)//node:current node; position:next position in the string
        {
            if (node == null) return false; //search miss

            if (position == key.length())
            {
                if (node.Key == null) return false;
                else return true;
            }
            return Getv(key, node.Next(key.charAt(position)), position + 1);
        }

    }

    private int[] score = {0, 0, 0, 1, 1, 2, 3, 5, 11, 11, 11, 11, 11};
    private boolean[][] captured;
    private RWayTries Trie;

    private void DFS(BoggleBoard board, HashSet<String> set, int row, int col, RWayTries.Node node)
    {

        if (row >= board.rows() || row < 0 || col >= board.cols() || col < 0) return; //out of bounds check
        if (captured[row][col]) return; // visited check
        char c = board.getLetter(row, col);
        RWayTries.Node nextNode;
        if (c == 'Q')
        {
            if (node.Next('Q') == null) return;
            else nextNode = node.Next('Q').Next('U');
        } else nextNode = node.Next(c);
        if (nextNode == null) return; //prune if is not a prefix of any word
        captured[row][col] = true; //capture
        if (nextNode.Key != null && nextNode.Key.length() > 2) set.add(node.Next(c).Key);
        DFS(board, set, row + 1, col, nextNode);
        DFS(board, set, row - 1, col, nextNode);
        DFS(board, set, row, col + 1, nextNode);
        DFS(board, set, row, col - 1, nextNode);
        DFS(board, set, row + 1, col + 1, nextNode);
        DFS(board, set, row - 1, col + 1, nextNode);
        DFS(board, set, row - 1, col - 1, nextNode);
        DFS(board, set, row + 1, col - 1, nextNode);
        captured[row][col] = false; //reset capture
    }

    public BoggleSolver(String[] dictionary)
    {
        Trie = new RWayTries();
        for (String s : dictionary)
        {
            Trie.Put(s);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        HashSet<String> queue = new HashSet<>();
        captured = new boolean[board.rows()][board.cols()];
        for (int i = 0; i != board.rows(); ++i)
        {
            for (int j = 0; j != board.cols(); ++j)
            {
                DFS(board, queue, i, j, Trie.root);
            }
        }
        return queue;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
        int val;
        if (word.length() > 10) val = 11;
        else val = score[word.length()];
        if (!Trie.Get(word)) val = 0;
        return val;
    }

    public static void main(String[] args)
    {
        String[] dict = new String[6013];
        In in = new In("dictionary-algs4.txt");
        for (int i = 0; i != 6013; ++i)
        {
            dict[i] = in.readString();
        }
        BoggleSolver solver = new BoggleSolver(dict);
        StdOut.println(solver.scoreOf("SATISFACTION"));

    }
}
