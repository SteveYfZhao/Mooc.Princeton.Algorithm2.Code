/**
 * Created by Zhao on 2015-11-05.
 */

import edu.princeton.cs.algs4.BTree;

public class Outcast {
    private WordNet wNet;
    private BTree<String, Integer> cache;

    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        wNet = wordnet;
        cache = new BTree<String, Integer>();
    }

    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        int dMax = -1;
        String cast = null;
        for (String noun1 : nouns) {
            int d = 0;
            for (String noun2 : nouns) {
                int di = 0;
                if (noun1 != noun2) {
                    String key1 = noun1 + "," + noun2;
                    String key2 = noun2 + "," + noun1;
                    if (cache.get(key1) == null) {
                        di = wNet.distance(noun1, noun2);
                        cache.put(key1, di);
                        cache.put(key2, di);
                    } else {
                        di = cache.get(key1);
                    }
                }
                d += di;
            }
            if (d > dMax) {
                dMax = d;
                cast = noun1;
            }
        }
        return cast;
    }

    public static void main(String[] args)  // see test client below
    {

    }
}
