/* *****************************************************************************
 *  Name:Momoko Hinanawi
 *  Date:2019.8.12
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board
{
    private int[][] array;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles)
    {
        array = new int[tiles.length][tiles.length]; // defensive copying
        for (int i = 0; i != tiles.length; ++i)
            for (int j = 0; j != tiles.length; ++j)
            {
                array[i][j] = tiles[i][j];
            }
    }

    // string representation of this board
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(array.length);
        for (int[] line : array)
        {
            sb.append('\n');
            for (int i : line)
            {
                sb.append(" " + i);
            }
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension()
    {
        return array.length;
    }

    // number of tiles out of place
    public int hamming()
    {
        int ret = 0;
        int count = 1;
        for (int i = 0; i != array.length; ++i)
        {
            for (int j = 0; j != array.length; ++j)
            {
                if (i == dimension() - 1 && j == dimension() - 1)
                {
                    return ret;
                }
                if (array[i][j] != count)
                {
                    ++ret;
                }
                ++count;
            }
        }
        return ret;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        int ret = 0;
        for (int i = 0; i != array.length; ++i)
        {
            for (int j = 0; j != array.length; ++j)
            {
                if (array[i][j] == 0) continue;
                int x = (array[i][j] - 1) / dimension();
                int y = (array[i][j] - 1) % dimension();
                ret += (Math.abs(i - x) + Math.abs(j - y));


            }
        }
        return ret;
    }

    // is this board the goal board?
    public boolean isGoal()
    {
        int counter = 1;
        for (int i = 0; i != array.length; ++i)
        {
            for (int j = 0; j != array.length; ++j)
            {
                if (array[i][j] != counter)
                {
                    if (!(i == dimension() - 1 && j == dimension() - 1))
                    {
                        return false;
                    }
                }
                ++counter;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == null) return false;
        if (this == y) return true;
        if (y.getClass() != this.getClass())
        {
            return false;
        }
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        for (int i = 0; i != array.length; ++i)
        {
            for (int j = 0; j != array.length; ++j)
            {
                if (this.array[i][j] != that.array[i][j])
                {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        int x = 0, y = 0;
        outer:
        for (int i = 0; i != array.length; ++i)
        {
            for (int j = 0; j != array.length; ++j)
            {
                if (array[i][j] == 0)
                {
                    x = i;
                    y = j;
                    break outer;
                }
            }
        }
        // Blank square:[x][y]
        Stack<Board> boardStack = new Stack<>();
        if (isValid(x - 1, y))
        {
            Board newBoard = new Board(this.array);
            newBoard.array[x][y] = newBoard.array[x - 1][y];
            newBoard.array[x - 1][y] = 0;
            // StdOut.println(newBoard.toString());
            boardStack.push(newBoard);
        }
        if (isValid(x + 1, y))
        {
            Board newBoard = new Board(this.array);
            newBoard.array[x][y] = newBoard.array[x + 1][y];
            newBoard.array[x + 1][y] = 0;
            // StdOut.println(newBoard.toString());
            boardStack.push(newBoard);
        }
        if (isValid(x, y - 1))
        {
            Board newBoard = new Board(this.array);
            newBoard.array[x][y] = newBoard.array[x][y - 1];
            newBoard.array[x][y - 1] = 0;
            // StdOut.println(newBoard.toString());
            boardStack.push(newBoard);
        }
        if (isValid(x, y + 1))
        {
            Board newBoard = new Board(this.array);
            newBoard.array[x][y] = newBoard.array[x][y + 1];
            newBoard.array[x][y + 1] = 0;
            // StdOut.println(newBoard.toString());
            boardStack.push(newBoard);
        }
        return boardStack;
    }

    private boolean isValid(int x, int y)
    {
        return x >= 0 && y >= 0 && x < dimension() && y < dimension();
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        Board board = new Board(array);
        if (board.array[0][0] == 0)
        {
            int c = board.array[1][0];
            board.array[1][0] = board.array[1][1];
            board.array[1][1] = c;
            return board;
        }
        if (board.array[0][1] == 0)
        {
            int c = board.array[0][0];
            board.array[0][0] = board.array[1][0];
            board.array[1][0] = c;
            return board;
        }
        int c = board.array[0][0];
        board.array[0][0] = board.array[0][1];
        board.array[0][1] = c;
        return board;
    }

    // unit testing (not graded)
    public static void main(String[] args)
    {
        Board board = new Board(new int[][]{{1, 0, 8}, {4, 2, 3}, {7, 6, 5}});
        StdOut.println(board.toString());
        StdOut.println(board.twin().toString());
    }

}
