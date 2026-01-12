import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Bipartite;
import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

public class SecondAssignment {
    static Digraph readGraph(String path) {
        return new Digraph(new In(path));
    }

    static Graph asUndirected(Digraph dg) {
        Graph ug = new Graph(dg.V());
        for (int v = 0; v < dg.V(); v++) {
            for (int w : dg.adj(v))
                ug.addEdge(v, w);
        }
        return ug;
    }

    static List<List<Integer>> weakComponents(Digraph dg) {
        Graph ug = asUndirected(dg);
        CC cc = new CC(ug);
        List<List<Integer>> res = new ArrayList<>();

        for (int i = 0; i < cc.count(); i++)
            res.add(new ArrayList<>());
        for (int v = 0; v < ug.V(); v++)
            res.get(cc.id(v)).add(v);
        return res;
    }

    static Iterable<Integer> directedCycle(Digraph dg) {
        DirectedCycle dc = new DirectedCycle(dg);
        return dc.hasCycle() ? dc.cycle() : null;
    }

    static Iterable<Integer> topoIfPossible(Digraph dg) {
        Topological t = new Topological(dg);
        return t.hasOrder() ? t.order() : null;
    }

    static boolean isBipartiteIgnoringDirection(Digraph dg) {
        Graph ug = asUndirected(dg);
        return new Bipartite(ug).isBipartite();
    }

    public static void main(String[] args) {
        Digraph dg = readGraph(args[0]);
        List<List<Integer>> comps = weakComponents(dg);
        System.out.println("Number of weak components: " + comps.size());
        System.out.println("Weak components" + comps);

        System.out.println("Cycle Directed: " + directedCycle(dg));
        System.out.println("Topology: " + topoIfPossible(dg));

        System.out.println("Bipartite: " + isBipartiteIgnoringDirection(dg));

    }
}