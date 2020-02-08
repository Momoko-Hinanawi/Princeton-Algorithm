/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront
{
    private static int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode()
    {
        String str = BinaryStdIn.readString();
        LinkedList<Integer> list = new LinkedList<>(); //the rearranged string
        for (int i = 0; i != R; ++i)
        {
            list.add(i);
        }
        for (int i = 0; i != str.length(); ++i)
        {
            int index = list.indexOf((int) str.charAt(i)); //select the inputed char
            BinaryStdOut.write((char) index, 8); //output its index
            int kebab = list.remove(index); //move it to the first position
            list.add(0, kebab);
        }
        BinaryStdOut.close();

    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
        String str = BinaryStdIn.readString();
        LinkedList<Integer> list = new LinkedList<>(); //the rearranged string
        for (int i = 0; i != R; ++i)
        {
            list.add(i);
        }
        for (int i = 0; i != str.length(); ++i)
        {
            char c = str.charAt(i);
            BinaryStdOut.write(list.get((int) c), 8);
            int kebab = list.remove((int) c);
            list.add(0, kebab);

        }
        BinaryStdOut.close();

    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args)
    {
        if (args[0].equals("-"))
            MoveToFront.encode();
        else
            MoveToFront.decode();
    }
}

