import edu.princeton.cs.algs4.*;
import java.util.Date;
import java.lang.*;
import java.util.HashSet;


/**
 * Created by Zhao on 2015-12-13.
 */
public class BoggleSolver {
    private TrieMini wordsTrie;
    private boolean[][] visited;
    private int cols = 0;
    private int rows = 0;

    private HashSet resultSet;
    private Queue<String> resultQueue;

    private TrieMini results;
    private TrieMini nonresults;


    private int getTrieNum(String word) {
        char first = word.charAt(0);
        char second = word.charAt(1);
        return getCharNum(first) * 26 + getCharNum(second);

    }

    private int getCharNum(char letter) {
        if ('A' <= letter && letter <= 'Z')
            return letter - 'A';

        return -1;

    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        wordsTrie = new TrieMini();

        for (String word : dictionary) {
            if (word.length() > 1) {
                wordsTrie.add(word);
            }
        }
    }

    // helper region
    private Bag<int[]> getAvailDices(int i, int j) {
        Bag<int[]> dices = new Bag<int[]>();
        for (int x = i - 1; x <= i + 1; x++) {
            if (x > -1 && x < cols) {
                for (int y = j - 1; y <= j + 1; y++) {
                    if (y > -1 && y < rows) {
                        if ((y == j && x == i) || visited[x][y]) {

                        } else {
                            int[] dice = {x, y};
                            dices.add(dice);
                        }
                    }
                }
            }
        }
        return dices;
    }

    private int growPath(int i, int j, StringBuilder temp, BoggleBoard board, Node node) {
        int wordNum = 0;
        Bag<int[]> dices = getAvailDices(i, j);
        //StdOut.println("continue for " + temp);


        for (int[] dice : dices) {
            int x = dice[0];
            int y = dice[1];


            wordNum = wordNum + getNeighbor(x, y, temp, board, node);


        }
        return wordNum;
    }

    // helper region ends


    private int getNeighbor(int i, int j, StringBuilder prefix, BoggleBoard board, Node node) {
        //StdOut.println(" trieId = " + trieId ) ;
        int wordNum = 0;


        //StdOut.println("Visited i: " + i + " j: " + j +" - " + visited[i][j]);

        visited[i][j] = true;
        char letter = board.getLetter(j, i);

        prefix = prefix.append(letter);
        node = wordsTrie.getChild(node, letter);

        boolean hasQ =  false;

        if (node != null && letter == 'Q') {
            hasQ = true;
            prefix = prefix.append('U');
            node = wordsTrie.getChild(node, 'U');
        }

        if (node != null) {

            if (node.isString) {
                //StdOut.println("Builder Length: " + prefix.length());
                String prefStr = prefix.toString();
                if (prefix.length() > 2 && !resultSet.contains(prefStr)) {
                    resultSet.add(prefStr);
                    results.add(prefStr);
                    resultQueue.enqueue(prefStr);
                    wordNum++;
                }
            }

            growPath(i, j, prefix, board, node);

        }

        prefix.deleteCharAt(prefix.length() - 1);
        if (hasQ)
        {
            prefix.deleteCharAt(prefix.length() - 1);
        }
        visited[i][j] = false;
        return wordNum;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        //get size
        int w = board.cols();
        int h = board.rows();
        rows = h;
        cols = w;
        visited = new boolean[cols][rows];
        resultSet = new HashSet<String>();
        results = new TrieMini();
        nonresults = new TrieMini();

        int wordNum = 0;

        Node root = wordsTrie.getRoot();

        StringBuilder empty = new StringBuilder();

        // loop through every block
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                wordNum = getNeighbor(i, j, empty, board, root);
            }
        }
        return resultSet;
    }

    private boolean wordExist(String word) {

        return wordsTrie.contains(word);

    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {


        int len = word.length();
        if (len <= 2)
            return 0;
        if (!wordExist(word)) {
            return 0;
        }
        if (len <= 4)
            return 1;
        if (len == 5)
            return 2;
        if (len == 6)
            return 3;
        if (len == 7)
            return 5;
        if (len >= 8)
            return 11;
        return -1;
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleBoard board = new BoggleBoard(args[1]);
        Date startTime = new Date();
        BoggleSolver solver = new BoggleSolver(dictionary);

        StdOut.print(board.toString());
        int score = 0;
        Date midTime = new Date();
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        Date endTime = new Date();
        long init = midTime.getTime() - startTime.getTime();
        long mids = endTime.getTime() - midTime.getTime();
        long ms = endTime.getTime() - startTime.getTime();
        StdOut.println("Initialize: " + String.valueOf(init));
        StdOut.println("Loop Millisec: " + String.valueOf(mids));
        StdOut.println("Total Millisec: " + String.valueOf(ms));


    }
}
