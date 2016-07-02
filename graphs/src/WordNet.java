/**
 * Created by Zhao on 2015-11-04.
 */

import edu.princeton.cs.algs4.*;

import java.io.*;

public class WordNet {

    private Digraph dG;

    private BST<String, Bag<Integer>> word2Id;
    private BTree<Integer, String> id2synset;
    private int wordNum;
    private int maxId = 0;
    private SAP sapHelper;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        In in = new In(synsets);
        String line = in.readLine();
        id2synset = new BTree<Integer, String>();
        word2Id = new BST<String, Bag<Integer>>();
        while (line != null) {
            String[] tokens = line.split(",");
            int i = Integer.parseInt(tokens[0]);
            String synset = tokens[1];
            //StdOut.println(i);
            //StdOut.println(synset);
            id2synset.put(i, synset);
            String[] nouns = synset.split(" ");
            for (String noun : nouns) {
                if (word2Id.contains(noun)) {
                    Bag<Integer> ids = word2Id.get(noun);
                    ids.add(i);
                } else {
                    Bag<Integer> ids = new Bag<Integer>();
                    ids.add(i);
                    word2Id.put(noun, ids);
                }

            }


            if (i > maxId) {
                maxId = i;
            }
            line = in.readLine();
        }
        dG = new Digraph(maxId + 1);

        In in2 = new In(hypernyms);
        String line_gf = in2.readLine();
        while (line_gf != null) {
            String[] tokens = line_gf.split(",");
            int v = Integer.parseInt(tokens[0]);
            int j = tokens.length;
            for (int i = 1; i < j; i++) {
                int w = Integer.parseInt(tokens[i]);
                dG.addEdge(v, w);
            }
            line_gf = in2.readLine();
        }

        sapHelper = new SAP(dG);

        //cycle detect
        DirectedCycle cycDetect = new DirectedCycle(dG);
        if (cycDetect.hasCycle()) {
            throw new IllegalArgumentException("Cycles detected.");
        }

        // Root test
        int rootNum = 0;
        for (int i = 0; i < dG.V(); i++) {
            if (dG.indegree(i) > 0 && dG.outdegree(i) == 0) {
                rootNum++;
            }
        }
        if (rootNum != 1) {
            throw new IllegalArgumentException("Multiroot detected.");
        }

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return word2Id.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return word2Id.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {

        if (!this.isNoun(nounA) || !this.isNoun(nounB)) {
            throw new IllegalArgumentException("Not a word.");
        }

        Bag<Integer> v = word2Id.get(nounA);
        Bag<Integer> w = word2Id.get(nounB);


        return sapHelper.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!this.isNoun(nounA) || !this.isNoun(nounB)) {
            throw new IllegalArgumentException("Not a word.");
        }
        Bag<Integer> v = word2Id.get(nounA);
        Bag<Integer> w = word2Id.get(nounB);
        int anceId = sapHelper.ancestor(v, w);
        return id2synset.get(anceId);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("E:\\synsets15.txt", "E:\\hypernyms15Path.txt");
        for (String word : wn.nouns()) {
            StdOut.println(word);
        }

    }

}
