public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, World!");

        ColorableGraph g = new ColorableGraph(9);

        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(1, 7);
        g.addEdge(2, 3);
        g.addEdge(2, 5);
        g.addEdge(2, 8);
        g.addEdge(3, 6);
        g.addEdge(3, 9);
        g.addEdge(4, 5);
        g.addEdge(4, 6);
        g.addEdge(4, 7);
        g.addEdge(5, 6);
        g.addEdge(5, 8);
        g.addEdge(6, 9);
        g.addEdge(7, 8);
        g.addEdge(7, 9);
        g.addEdge(8, 9);



        int[] colors = g.initialColorGreedyByDegree();
        colors = g.fixBySwap();

        for(int color : colors) {
            System.out.print(color + " ");
        }
        System.out.println();

        return;
    }
}
