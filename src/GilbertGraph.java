import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GilbertGraph {
    private static final class Node {
        public final List<Integer> neighbours;

        public void addNeighbour(int neighbour) {
            this.neighbours.add(neighbour);
        }

        public int getDegree() {
            return this.neighbours.size();
        }

        public Node() {
            this.neighbours = new ArrayList<>();
        }
    }

    private final List<Node> nodes;
    private final int n;
    private final float p;
    private boolean generated;

    public void generate() {
        for (int i = 0; i < this.n; i++) {
            // TODO: thread this shit
            for (int j = 0; j < this.n; j++) {
                if (Math.random() <= this.p) {
                    // bidirectional graph
                    this.nodes.get(i).addNeighbour(j);
                    this.nodes.get(j).addNeighbour(i);
                }
            }
        }
        this.generated = true;
    }

    public float getAverageNodeDegree() {
        if (!this.generated) {
            return 0;
        }

        int degree = 0;
        for (int i = 0; i < this.n; i++) {
            degree += this.nodes.get(i).getDegree();
        }
        return ((float) degree) / ((float) this.n);
    }

    public int getPathLength(int from, int to) {
        int pred[] = new int[this.n];
        int dist[] = new int[this.n];
        if (!this.BFS(this.nodes, from, to, pred, dist)) {
            return 0;
        }

        // LinkedList to store path
        LinkedList<Integer> path = new LinkedList<Integer>();
        int crawl = to;
        path.add(crawl);
        while (pred[crawl] != -1) {
            path.add(pred[crawl]);
            crawl = pred[crawl];
        }

        // Print distance
        //System.out.println("Shortest path length is: " + dist[to]);

        // Print path
        //System.out.println("Path is ::");
        for (int i = path.size() - 1; i >= 0; i--) {
            //System.out.print(path.get(i) + " ");
        }
        //System.out.print("\n");

        return path.size();
    }

    // stolen from g4g
    private boolean BFS(List<Node> nodes, int src,
                               int dest, int[] pred, int[] dist)
    {
        LinkedList<Integer> queue = new LinkedList<>();

        boolean visited[] = new boolean[this.n];

        for (int i = 0; i < this.n; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }

        visited[src] = true;
        dist[src] = 0;
        queue.add(src);

        // bfs
        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < nodes.get(u).neighbours.size(); i++) {
                if (visited[nodes.get(u).neighbours.get(i)] == false) {
                    visited[nodes.get(u).neighbours.get(i)] = true;
                    dist[nodes.get(u).neighbours.get(i)] = dist[u] + 1;
                    pred[nodes.get(u).neighbours.get(i)] = u;
                    queue.add(nodes.get(u).neighbours.get(i));

                    if (nodes.get(u).neighbours.get(i) == dest)
                        return true;
                }
            }
        }
        return false;
    }

    public float getAveragePathLength(int samples) {
        float length = 0.0f;
        for (int i = 0; i < samples; i++) {
            // choose two nodes randomly
            int from = (int) Math.floor(Math.random() * this.n);
            int to = (int) Math.floor(Math.random() * this.n);
            if (from == to) {
                i--;
                continue;
            }

            length += this.getPathLength(from, to);
        }
        return length / samples;
    }

    public GilbertGraph(int n, float p) {
        if (p < 0 || p > 1) {
            throw new InvalidParameterException("p must be a value between 0 and 1, got " + p);
        }

        this.n = n;
        this.p = p;

        this.nodes = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            this.nodes.add(new Node());
        }
    }
}
