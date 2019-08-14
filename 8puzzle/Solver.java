/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver
{

    private MinPQ<Node> nodeMinPQ = new MinPQ<Node>(Node::compareTo);
    private int count = 0;
    private Node lastNode = null;

    private class Node implements Comparable<Node>
    {
        private Board board;
        private int moveSoFar = 0;
        private boolean isHamming = false;
        private Node previous = null;

        Node(Board b, Node prev, int move, boolean hamming)
        {
            board = b;
            moveSoFar = move;
            isHamming = hamming;
            previous = prev;
        }

        int priority()
        {
            if (isHamming)// true:hamming;false:manhattan
            {
                return moveSoFar + board.hamming();
            } else
            {
                return moveSoFar + board.manhattan();
            }
        }

        @Override
        public int compareTo(Node that)
        {
            return Integer.compare(priority(), that.priority());
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        Node firstNode = new Node(initial, null, 0, false);
        Node firstNode1 = new Node(initial.twin(), null, 0, false);
        MinPQ<Node> nodeMinPQ1 = new MinPQ<>(Node::compareTo);
        nodeMinPQ.insert(firstNode);
        nodeMinPQ1.insert(firstNode1);
        while (!nodeMinPQ.isEmpty())
        {
            // /Process the board/ //
            Node last = nodeMinPQ.delMin();
            if (last.board.isGoal())
            {
                count = last.moveSoFar;
                lastNode = last;
                break;
            }
            for (Board b : last.board.neighbors())
            {

                if (last.previous != null)
                {
                    if (last.previous.board.equals(b))
                    {
                        continue;
                    }
                }
                nodeMinPQ.insert(new Node(b, last, last.moveSoFar + 1, false));
            }
            // /Process the board's twin/ //
            if (!nodeMinPQ1.isEmpty())
            {
                Node last1 = nodeMinPQ1.delMin();
                if (last1.board.isGoal())
                {
                    count = -1;
                    return;
                }
                for (Board b : last1.board.neighbors())
                {
                    if (last1.previous != null)
                    {
                        if (last1.previous.board.equals(b))
                        {
                            continue;
                        }
                    }
                    nodeMinPQ1.insert(new Node(b, last1, last1.moveSoFar + 1, false));
                }
            }

        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable()
    {
        return !(count == -1);
    }

    // min number of moves to solve initial board
    public int moves()
    {
        return count;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution()
    {
        Stack<Board> stack = new Stack<>();
        Node ptr = lastNode;
        while (ptr != null)
        {
            stack.push(ptr.board);
            ptr = ptr.previous;
        }
        return stack;
    }

    // test client (see below)
    public static void main(String[] args)
    {
        Board board = new Board(new int[][]{{0, 1, 3}, {4, 2, 5}, {7, 8, 6}});
        Solver solver = new Solver(board);
        StdOut.println(solver.count);

    }
}
