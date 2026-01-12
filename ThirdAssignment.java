import java.util.ArrayList;
import java.util.List;


import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedDFS;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.TransitiveClosure;

public class ThirdAssignment {
    static Digraph readDigraph(String path){
        return new Digraph(new In(path));
    }
    static Graph readGraph(String path){
        return new Graph(new In(path));
    }
    static List<Integer> sources(Digraph g){
        List<Integer> res = new ArrayList<>();
        for(int v = 0; v < g.V(); v++){
            if(g.indegree(v) == 0)
                res.add(v);
        }
        return res;
    }
    static List<Integer> sinks(Digraph g){
        List<Integer> res = new ArrayList<>();
        for(int v = 0; v < g.V(); v++){
            if(g.outdegree(v) == 0)
                res.add(v);
        }
        return res;
    }

    static boolean canReachAllFrom(Digraph g, int s){
        DirectedDFS dfs = new DirectedDFS(g, s);
        for(int v = 0 ; v < g.V(); v++){
            if(!dfs.marked(v))
                return false;
        }
        return true;
    }
    static Integer vertexThatReachesAll(Digraph g){
        for(int s = 0; s < g.V(); s++){
            if(canReachAllFrom(g, s))
                return s;
        }
        return null;
    }
    static Integer vertexReachableFromAll(Digraph g){
        Digraph r = g.reverse();
        for(int t = 0; t < r.V(); t++){
            if(canReachAllFrom(r, t))
                return t;
        }
        return null;
    }
    static boolean canReturnToSelf(Digraph g, int v){
        for(int w : g.adj(v)){
            if(w == v)
                return true;
        }
        DirectedDFS fromV = new DirectedDFS(g, v);
        DirectedDFS toV = new DirectedDFS(g.reverse(), v);
        for(int u = 0; u < g.V(); u++){
            if(u != v && fromV.marked(u) && toV.marked(u))
                return true;
        }
        return false;
    }
    static Digraph transitiveClosureGraph(Digraph g){
        TransitiveClosure tc = new TransitiveClosure(g);
        Digraph closure = new Digraph(g.V());
        for(int v = 0; v < g.V(); v++){
            for(int w = 0; w < g.V(); w++){
                if(v != w && tc.reachable(v, w))
                    closure.addEdge(v, w);
            }
        }
        return closure;
    }
    static Graph spanningTreeUndirectedOrThrow(Graph g) {
        CC cc = new CC(g);
        if (cc.count() != 1) throw new IllegalStateException("Graph not connected.");

        int s = 0;
        boolean[] marked = new boolean[g.V()];
        int[] parent = new int[g.V()];
        for (int i = 0; i < parent.length; i++) parent[i] = -1;

        Queue<Integer> q = new Queue<>();
        q.enqueue(s);
        marked[s] = true;

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : g.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    parent[w] = v;
                    q.enqueue(w);
                }
            }
        }

        Graph tree = new Graph(g.V());
        for (int v = 0; v < g.V(); v++) {
            if (v == s) continue;
            tree.addEdge(parent[v], v);
        }
        return tree;
    }


    static Digraph rootedSpanningTreeOrNull(Digraph g) {
        Integer root = vertexThatReachesAll(g);
        if (root == null) return null;

        boolean[] marked = new boolean[g.V()];
        int[] parent = new int[g.V()];
        for (int i = 0; i < parent.length; i++) parent[i] = -1;

        Queue<Integer> q = new Queue<>();
        q.enqueue(root);
        marked[root] = true;

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : g.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    parent[w] = v;
                    q.enqueue(w);
                }
            }
        }

        for (int v = 0; v < g.V(); v++) {
            if (!marked[v])
                throw new IllegalStateException("Root does not reach all v");
        }

        Digraph tree = new Digraph(g.V());
        for (int v = 0; v < g.V(); v++) {
            if (v == root)
                continue;
            tree.addEdge(parent[v], v);
        }
        return tree;
    }

    public static void main(String[] args) {
        String mode = args[0];
        String file = args[1];

        if (mode.equals("digraph")) {
            Digraph g = readDigraph(file);

            System.out.println("Sources: " + sources(g));
            System.out.println("Sinks " + sinks(g));

            Integer mother = vertexThatReachesAll(g);
            System.out.println("Vertex that reaches all: " + (mother != null) + " (example: " + mother + ")");

            Integer sinkAll = vertexReachableFromAll(g);
            System.out.println("Vertex t " + (sinkAll != null) + " (primer: " + sinkAll + ")");

            if (args.length >= 3) {
                int v = Integer.parseInt(args[2]);
                System.out.println("Vertex: " + v + " can reach all from: " + canReachAllFrom(g, v));
                System.out.println("Vertex: " + v + " can return to self: " + canReturnToSelf(g, v));
            }

            Digraph tcg = transitiveClosureGraph(g);
            System.out.println("Transitive Closure (digraph):");
            System.out.println(tcg);

            Digraph rootedTree = rootedSpanningTreeOrNull(g);
            System.out.println("Rooted Sapnning Tree or Null:");
            System.out.println(rootedTree == null ? "null" : rootedTree);

        } else if (mode.equals("graph")) {
            Graph g = readGraph(file);
            Graph tree = spanningTreeUndirectedOrThrow(g);
            System.out.println("Spanning Tree Undirected:");
            System.out.println(tree);
        } else {
            System.out.println("Unknown mode: " + mode);
        }
    }
}