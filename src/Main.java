public class Main {
    private static void make(int n, float p) {
        final GilbertGraph graph = new GilbertGraph(n, p);
        graph.generate();
        System.out.println("Degrees " + n + "/" + p + ": " + graph.getAverageNodeDegree());
        System.out.println("Avg. Path Length: " + graph.getAveragePathLength(100));
    }

    public static void main(String[] args) {
        // generate graphs with logarithmic progression of probability
        make(100, 0.01f);
        make(1000, 0.001f);
        make(10000, 0.0001f);
        make(100000, 0.00001f);

        /*
         * Degrees 100/0.01: 1.94
         * Avg. Path Length: 2.73
         * Degrees 1000/0.001: 1.908
         * Avg. Path Length: 6.15
         * Degrees 10000/1.0E-4: 2.0042
         * Avg. Path Length: 7.76
         */
        // so I guess logarithmic scaling of values?
        // p = 1/n for instance?
        // avg. path length increases
    }
}
