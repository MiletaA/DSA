import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.DijkstraUndirectedSP;


public class FourthAssignment {
    static EdgeWeightedGraph read(String path){
        return new EdgeWeightedGraph(new In(path));
    }
    static int maxIncident(EdgeWeightedGraph g){
        double best = -1;
        int vBest = -1;
        for(int v = 0; v < g.V(); v++){
            double sum = 0;
            for(Edge e : g.adj(v)){
                sum += e.weight();
            }
            if(sum > best){
                best = sum;
                vBest = v;
            }
        }
        return vBest;
    }
    static int minIncident(EdgeWeightedGraph g){
        double best = Double.POSITIVE_INFINITY;
        int vBest = -1;
        for(int v = 0; v < g.V(); v++){
            double sum = 0;
            for(Edge e : g.adj(v)){
                sum += e.weight();
            }
            if(sum < best){
                best = sum;
                vBest = v;
            }
        }
        return vBest;
    }
    static void printAllShortestPaths(EdgeWeightedGraph g, int s) {
        DijkstraUndirectedSP sp = new DijkstraUndirectedSP(g, s);
        for (int v = 0; v < g.V(); v++) {
            if (sp.hasPathTo(v)) {
                System.out.println(s + " -> " + v + " dist=" + sp.distTo(v) + " path=" + sp.pathTo(v));
            } else {
                System.out.println(s + " -> " + v + " no path");
            }
        }
    }
    static boolean canDiveTotalWithin(EdgeWeightedGraph g, int s, int t, double n) {
        DijkstraUndirectedSP sp = new DijkstraUndirectedSP(g, s);
        return sp.hasPathTo(t) && sp.distTo(t) <= n;
    }
static boolean canDiveEachEdgeWithin(EdgeWeightedGraph g, int s, int t, double n) {
        boolean[] marked = new boolean[g.V()];
        Queue<Integer> q = new Queue<>();
        q.enqueue(s);
        marked[s] = true;

        while (!q.isEmpty()) {
            int v = q.dequeue();
            if (v == t)
                return true;

            for (Edge e : g.adj(v)) {
                if (e.weight() > n)
                    continue;
                int w = e.other(v);
                if (!marked[w]) {
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
        return false;
    }
    static boolean canDiveSurfaceEvenOnly(EdgeWeightedGraph g, int s, int t, double n) {
        boolean[] isSurface = new boolean[g.V()];
        for (int v = 0; v < g.V(); v++) isSurface[v] = (v % 2 == 0);
        isSurface[s] = true;
        isSurface[t] = true;

        boolean[] visitedSurface = new boolean[g.V()];
        Queue<Integer> q = new Queue<>();
        q.enqueue(s);
        visitedSurface[s] = true;

        while (!q.isEmpty()) {
            int cur = q.dequeue();
            if (cur == t) return true;

            DijkstraUndirectedSP sp = new DijkstraUndirectedSP(g, cur);
            for (int v = 0; v < g.V(); v++) {
                if (!isSurface[v] || visitedSurface[v] || v == cur)
                    continue;
                if (sp.hasPathTo(v) && sp.distTo(v) <= n) {
                    visitedSurface[v] = true;
                    q.enqueue(v);
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        EdgeWeightedGraph g = read(args[0]);
        int s = Integer.parseInt(args.length >= 2 ? args[1] : "0");
        int t = Integer.parseInt(args.length >= 3 ? args[2] : "5");
        double n = Double.parseDouble(args.length >= 4 ? args[3] : "10");

        DijkstraUndirectedSP sp = new DijkstraUndirectedSP(g, s);

        System.out.println("Shortest Path: " + (sp.hasPathTo(t) ? sp.pathTo(t) : "no path"));
        System.out.println("Dis: " + (sp.hasPathTo(t) ? sp.distTo(t) : "INF"));

        System.out.println("Max incident: " + maxIncident(g));
        System.out.println("Min incident: " + minIncident(g));

        System.out.println("Shortest paths " + s + ":");
        printAllShortestPaths(g, s);

        System.out.println("Dive (Total <= n): " + canDiveTotalWithin(g, s, t, n));
        System.out.println("Dive (Each Edge <= n): " + canDiveEachEdgeWithin(g, s, t, n));
        System.out.println("Dive (Surface even, segment <= n): " + canDiveSurfaceEvenOnly(g, s, t, n));
    }
    }