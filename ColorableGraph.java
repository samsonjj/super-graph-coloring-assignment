import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class ColorableGraph {


    ArrayList<Vertex> vertices;
    ArrayList<Vertex> useThisToFindVertexByKey;
    int[] colors;

    public ColorableGraph(int size) {

        // init adjacency list and sorting list
        vertices = new ArrayList<>();
        useThisToFindVertexByKey = new ArrayList<>();
        for(int i = 0; i < size; i++) { vertices.add(new Vertex(i+1)); useThisToFindVertexByKey.add(new Vertex(i+1)); }

        // init colors
        colors = new int[size+1]; // we will use the key to store color, so since keys start at 1, we need size+1 spots if starting at 0
        Arrays.fill(colors, 0);

    }

    public Vertex getVertexByKey(int key) {
        return vertices.get(vertices.indexOf(useThisToFindVertexByKey.get(key-1)));
    }

    public void addEdge(int u, int v) throws IllegalArgumentException {
        if(u <= vertices.size() && v <= vertices.size() && u > 0 && v > 0) {
            getVertexByKey(u).adj.add(getVertexByKey(v));
            getVertexByKey(v).adj.add(getVertexByKey(u));
        } else {
            throw new IllegalArgumentException("edge vertices are not in range");
        }
    }

    public int[] initialColorGreedyByDegree() {
        Arrays.fill(colors, 0);

        // Welsh-Powell Algorithm
        Collections.sort(vertices, (u, v) -> v.adj.size() - u.adj.size());
        vertices.stream().forEach(System.out::println);

        boolean[] availableColors = new boolean[vertices.size() + 1];
        for(int i = 1; i <= vertices.size(); i++) {

            // set available colors
            Arrays.fill(availableColors, true);
            for(Vertex adjacent : getVertexByKey(i).adj) {
                if(colors[adjacent.key] != 0) availableColors[colors[adjacent.key]] = false;
            }

            // find first available color
            int firstAvailable = 1;
            while(!availableColors[firstAvailable]) {
                firstAvailable++;
            }

            // set color of i to that
            colors[i] = firstAvailable;
        }

        return colors;
    }

    /*public int[] initialColor() {
        Arrays.fill(colors, 0);

        // Welsh-Powell Algorithm
        Arrays.sort(adjacencyList, (a1, a2) -> a2.size() - a1.size());
        Arrays.stream(adjacencyList).forEach(System.out::println);

        boolean[] availableColors = new boolean[adjacencyList.length + 1];
        for(int i = 0; i < adjacencyList.length; i++) {

            // reset available colors
            Arrays.fill(availableColors, true);

            // set available colors
            for(Integer adjacent : adjacencyList[i]) {
                if(colors[adjacent] != 0) availableColors[colors[adjacent]] = false;
            }

            // find first available color
            int firstAvailable = 1;
            while(!availableColors[firstAvailable]) {
                firstAvailable++;
            }

            // set color of i to that
            colors[i] = firstAvailable;
        }

        return colors;
    }*/

    public int[] fixBySwap() {

        // init start time, for tracking time passed
        long startTime = System.currentTimeMillis();


        Random random = new Random();
        boolean[] availableColorsCurrent = new boolean[vertices.size()];
        int count = 0;
        boolean doneWithThisMaxVertex = false;
        boolean swappedRecently = true;
        boolean nextColorAvailable = true;

        // repeatedly find max vertex, and reduce, until out of time
        while(true) {
            if(System.currentTimeMillis() - startTime > 1000) break;

            // find vertex with max color
            Vertex currentVertex = vertices.get(0);
            for(int i = 1; i < vertices.size(); i++) {
                if(colors[vertices.get(i).key] > colors[currentVertex.key]) currentVertex = vertices.get(i);
            }

            System.out.println(currentVertex.key);

            // attempt to swap max vertex around until we can reduce, or count goes over 1000
            while (count < 1000) {

                // find available colors for current vertex
                Arrays.fill(availableColorsCurrent, true);
                for (Vertex adjacent : currentVertex.adj) {
                    if (colors[adjacent.key] != 0) availableColorsCurrent[colors[adjacent.key]] = false;
                }
                // if a lower color is available, switch to it and break
                for (int i = 1; i < colors[currentVertex.key]; i++) {
                    if (availableColorsCurrent[i] == true) {
                        colors[currentVertex.key] = i;
                        System.out.println("reducing!");
                        doneWithThisMaxVertex = true;
                        break;
                    }
                }
                if (doneWithThisMaxVertex) break;

                // okay this part will be the whole rest of the method
                // try to swap with one random next vertex
                nextColorAvailable = true;
                Vertex nextVertex = currentVertex.adj.get(random.nextInt(currentVertex.adj.size()));
                for(Vertex v : currentVertex.adj) {
                    if(v != nextVertex && colors[v.key] == colors[nextVertex.key]) {
                        nextColorAvailable = false;
                        break;
                    }
                }
                if(nextColorAvailable) {
                    // check if any vertex adjacent to the next vertex (excluding the current Vertex) has the color of currentVertex
                    for(Vertex v : nextVertex.adj) {
                        if(v != currentVertex && colors[v.key] == colors[currentVertex.key]) {
                            nextColorAvailable = false;
                            break;
                        }
                    }
                    if(nextColorAvailable) {
                        // now both slots are available for swapping
                        System.out.println("Swapping!");
                        int temp = colors[currentVertex.key];
                        colors[currentVertex.key] = colors[nextVertex.key];
                        colors[nextVertex.key] = colors[currentVertex.key];

                        currentVertex = nextVertex;
                    }
                }

                count++;
            }
        }

        return colors;
    }

    public int[] backTrackForMillis(long millis) {

        // init start time, for tracking time passed
        long startTime = System.currentTimeMillis();

        // init variables | backStack to track the backtrace, and currentVertex to know what vertex to evaluate during the loop
        ArrayList<Vertex> backStack = new ArrayList<>();
        Vertex currentVertex = null;

        while(System.currentTimeMillis() - startTime < millis) {
            if(currentVertex == null) {
                // not evaluating any vertex, look for vertex with max color
                Vertex vertexWithMaxColor = vertices.get(0);
                for(int i = 1; i < vertices.size(); i++) {
                    if(colors[vertices.get(i).key] > colors[vertexWithMaxColor.key]) vertexWithMaxColor = vertices.get(i);
                }

                currentVertex = vertexWithMaxColor;
            } else {
                // try to reduce color of current vertex
                // if can't, add it to the backStack
            }
        }

        return colors;
    }

    public void solveGraph() {
        this.initialColorGreedyByDegree();
    }
}
