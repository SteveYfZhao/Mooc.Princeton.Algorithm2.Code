import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * Created by Zhao on 2015-12-17.
 */
public class BurrowsWheeler {


    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode()
    {
        String s = BinaryStdIn.readString();
        //StdOut.println(s);
        CircularSuffixArray CSA = new CircularSuffixArray(s);

        int w = CSA.length();
        char[] input = s.toCharArray();
        char[] result = new char[w];

        int start = -1;

        for (int i = 0; i < w; i++ )
        {
            int j = CSA.index(i);
            if( j == 0 )
            {
                start = i;
            }
            result[i] = input[(j - 1 + w ) % w ];
        }
        BinaryStdOut.write(start);
        //StdOut.println(start);
        for (char ch : result)
        {
            BinaryStdOut.write(ch);
        }

        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode()
    {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int w = s.length();
        char[] t =  s.toCharArray();
        int[] next = new int[w];
        char[] sorted = new char[w];
        char[] origin = new char[w];

        // first pass on t[], get char distribution
        // second pass on t[], send chars to sorted, and
        // for each char, let i be index in t, j be index in sorted, h be index in next
        // if t[i] matches sorted[j], then next[j] = i

        int R = 256;   // extend ASCII alphabet size
        char[] aux = new char[w];

        // compute frequency counts
        int[] count = new int[R+1];
        for (int i = 0; i < w; i++)
            count[t[i] + 1]++;

        // compute cumulates
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];

        // move data
        for (int i = 0; i < w; i++) {
            int j = count[t[i]]++;
            //StdOut.println("j = " + j);

            sorted[j] = t[i];
            next[j] = i;
        }

        //recover origin
        int nextValue = first;
        for (int i = 0; i < w; i++) {
            char ch = sorted[nextValue];

            origin[i] = ch;
            BinaryStdOut.write(ch);
            //StdOut.print(ch);
            nextValue = next[nextValue];
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
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