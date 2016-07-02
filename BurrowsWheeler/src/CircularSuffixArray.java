import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Zhao on 2015-12-17.
 */
public class CircularSuffixArray {
    private String original;
    //private suffix[] sufficesArray;

    private int[] suffices;
    private int suffixLen;
    private boolean unary;

    /*private class suffix
    {
        public int start;
        //public int end;
        public int order;
        public char suffixCharAt(int i)
        {
            int j = (start + i) % suffixLen;
            return original.charAt(j);
        }
    }*/

    // Helper region

    private static char sufCharAt(int sufInd, int at, String s)
    {
        int sufLen = s.length();
        int j = (sufInd + at) % sufLen;
        return s.charAt(j);
    }

    private void genSuffices()
    {
        unary = true;
        char c = original.charAt(0);
        for (int i = 0; i < suffixLen; i++)
        {
            if (c !=  original.charAt(i))
                unary = false;
            suffices[i] = i;



        }
    }


    private static final int R             = 256;   // extended ASCII alphabet size
    private static final int CUTOFF        =  15;   // cutoff to insertion sort
    private static void sort(int[] a, String s) {
        int N = a.length;
        int[] aux = new int[N];
        sort(a, 0, N-1, 0, aux, s);
    }

    // return dth character of s, -1 if d = length of string


    // sort from a[lo] to a[hi], starting at the dth character
    private static void sort(int[] a, int lo, int hi, int d, int[] aux, String s) {

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d, s);
            return;
        }

        // compute frequency counts
        int[] count = new int[R+2];
        for (int i = lo; i <= hi; i++) {
            int c = sufCharAt(a[i],d, s);
            count[c+2]++;
        }

        // transform counts to indicies
        for (int r = 0; r < R+1; r++)
            count[r+1] += count[r];

        // distribute
        for (int i = lo; i <= hi; i++) {
            int c = sufCharAt(a[i], d, s);
            aux[count[c+1]++] = a[i];
        }

        // copy back
        for (int i = lo; i <= hi; i++)
            a[i] = aux[i - lo];


        // recursively sort for each character (excludes sentinel -1)
        for (int r = 0; r < R; r++)
            sort(a, lo + count[r], lo + count[r+1] - 1, d+1, aux, s);
    }


    // insertion sort a[lo..hi], starting at dth character
    private static void insertion(int[] a, int lo, int hi, int d, String s) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1], d, a.length, s); j--)
                exch(a, j, j-1);
    }

    // exchange a[i] and a[j]
    private static void exch(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // is v less than w, starting at character d
    private static boolean less(int v, int w, int d,int len, String s) {
        // assert v.substring(0, d).equals(w.substring(0, d));
        for (int i = d; i < len; i++) {
            if (sufCharAt(v,i,s) < sufCharAt(w,i,s)) return true;
            if (sufCharAt(v,i,s) > sufCharAt(w,i,s)) return false;
        }
        return false;
    }

    // Helper region ends

    public CircularSuffixArray(String s)  // circular suffix array of s
    {
        if (s ==null)
            throw new NullPointerException();
        original = s;
        suffixLen = original.length();
        //sufficesArray = new suffix[suffixLen];
        suffices = new int[suffixLen];

        if (s.length()>0) {
            genSuffices();
            //suffixLSD();
            if (!unary) {
                sort(suffices, original);
            }
        }
    }

    public int length()                   // length of s
    {
        return suffixLen;
    }
    public int index(int i)               // returns index of ith sorted suffix
    {
        if (i<0 || i >= suffixLen)
            throw new IndexOutOfBoundsException();
        return suffices[i];
    }
    public static void main(String[] args)// unit testing of the methods (optional)
    {
       // if (args[0].equals("-"))
        //{
           // encode();
       // }

        //String s = BinaryStdIn.readString();
        String s = "CCADBADB";
        //StdOut.println(s);

        CircularSuffixArray CSA = new CircularSuffixArray(s);
        for (int i = 0; i < CSA.length(); i++)
        {
            StdOut.println("");
            for (int j = 0; j < CSA.length(); j++)
            {
                StdOut.print(CSA.sufCharAt(CSA.suffices[i],j, s));
            }
            StdOut.print("    index: " + CSA.index(i) );
        }
    }
}
