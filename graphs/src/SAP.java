/**
 * Created by Zhao on 2015-11-06.
 */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BTree;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {

    private Digraph dG;
    private BTree<Integer, int[]> singleCacheForest;
    private int sapNum;
    private int anctor;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        dG = new Digraph(G);

    }

    private void bfs(int v, int w) {
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(dG, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(dG, w);
        sapNum = -1;
        anctor = -1;
        for (int i = 0; i < dG.V(); i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                if (sapNum == -1) {
                    anctor = i;
                    sapNum = bfs1.distTo(i) + bfs2.distTo(i);
                } else {
                    int steps = bfs1.distTo(i) + bfs2.distTo(i);
                    if (sapNum > steps) {
                        sapNum = steps;
                        anctor = i;
                    }
                }
            }
        }
    }

    private void bfs(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(dG, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(dG, w);
        sapNum = -1;
        anctor = -1;
        for (int i = 0; i < dG.V(); i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                if (sapNum == -1) {
                    anctor = i;
                    sapNum = bfs1.distTo(i) + bfs2.distTo(i);
                } else {
                    int steps = bfs1.distTo(i) + bfs2.distTo(i);
                    if (sapNum > steps) {
                        sapNum = steps;
                        anctor = i;
                    }
                }
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        bfs(v, w);
        return sapNum;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        bfs(v, w);
        return anctor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        bfs(v, w);
        return sapNum;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        bfs(v, w);
        return anctor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}
