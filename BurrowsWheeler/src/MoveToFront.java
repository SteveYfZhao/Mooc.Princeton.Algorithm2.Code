import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Zhao on 2015-12-17.
 */
public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
   /* private static char[] asciiTb;

    private static void popAscii()
    {
        asciiTb = new char[256];
        for (char code = 0; code < 256; code++ )
        {
            asciiTb[code]=code;
        }
    }*/
   /* private class Node
    {
        public char item;
        public Node next;
    }*/

    public static void encode()
    {
        char[] ascii = new char[256];
        for (char code = 0; code < 256; code++ )
        {
            ascii[code]=code;
        }
        while(!BinaryStdIn.isEmpty())
        {
            char c = BinaryStdIn.readChar();
            for (char j = 0; j < 256; j++) {
                if (ascii[j] == c) {
                    BinaryStdOut.write(j);
                    System.arraycopy(ascii, 0, ascii, 1, j);
                    ascii[0] = c;
                    break;
                }
            }
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
        char[] ascii = new char[256];
        for (char code = 0; code < 256; code++ )
        {
            ascii[code]=code;
        }
        StringBuilder sb= new StringBuilder();
        while(!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char h = ascii[c];
            //StdOut.print(h);
            //BinaryStdOut.write(h);
            sb.append(h);
            System.arraycopy(ascii, 0, ascii, 1, c);
            ascii[0] = h;
        }
        BinaryStdOut.write(sb.toString());
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args)
    {

        if (args[0].equals("-"))
        {
            encode();
        }
        if (args[0].equals("+"))
        {
            decode();
        }
    }
}
