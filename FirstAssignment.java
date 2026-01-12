import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Cycle;
import edu.princeton.cs.algs4.DepthFirstPaths;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Bipartite;

public class FirstAssignment {
    static Graph readGraph(String path){
        return new Graph(new In(path));
    }
    static List<Integer> componentFrom(Graph g, int s){

        CC cc = new CC(g);
        int id = cc.id(s);
        List<Integer> comp = new ArrayList<>();
        for(int v = 0; v < g.V(); v++){
            if (cc.id(v) == id)
                comp.add(v);
        }
        return comp;
    }
    static List<List<Integer>> allComponents(Graph g){
        CC cc = new CC(g);
        List<List<Integer>> res = new ArrayList<>();
        for(int i = 0; i < cc.count(); i++){
            res.add(new ArrayList<>());
        }
        for(int v = 0; v < g.V(); v++){
            res.get(cc.id(v)).add(v);
        }
        return res;
    }

    static boolean hasPath(Graph g, int s, int t){
        return new DepthFirstPaths(g, s).hasPathTo(t);
    }
    static Iterable<Integer> anyPath(Graph g, int s, int t){
        return new DepthFirstPaths(g, s).pathTo(t);
    }
  static int[] bfsDistances(Graph g, int s) {
        int[] dist = new int[g.V()];
        for (int i = 0; i < dist.length; i++) dist[i] = -1;

        Queue<Integer> q = new Queue<>();
        q.enqueue(s);
        dist[s] = 0;

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : g.adj(v)) {
                if (dist[w] == -1) {
                    dist[w] = dist[v] + 1;
                    q.enqueue(w);
                }
            }
        }
        return dist;
    }
    public static void main(String[] args) {
        Graph g = readGraph(args[0]);
        int s = 0, t = 3;

        System.out.println("Path: " + s + "->" + t + ": " + anyPath(g, s, t));
        System.out.println("Components: " + allComponents(g));
        System.out.println("Cycle: " + new Cycle(g).hasCycle());
        System.out.println("Bipartite: " + new Bipartite(g).isBipartite());
    }
    
}
