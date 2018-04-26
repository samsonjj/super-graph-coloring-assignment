import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ColorableGraphByAdjacentColors {

    ArrayList<Vertex> vertices;
    ArrayList<Vertex> useThisToFindVertexByKey;
    int[] colors;

    public ColorableGraphByAdjacentColors(int size) {

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

    // this can be made a lot faster (store the number when calculated)
    public int getNumAdjacentColored(Vertex v) {
        int numAdjacentColored = 0;
        for(Vertex u : v.adj) {
            if(colors[u.key] != 0) {
                numAdjacentColored++;
            }
        }
        return numAdjacentColored;
    }

    public int[] initialColor() {
        Arrays.fill(colors, 0);

        // store array of vertices left to color
        ArrayList<Vertex> verticesToColor = new ArrayList<>();
        for(int i = 0; i < vertices.size(); i++) { verticesToColor.add(new Vertex(i)); }

        Collections.copy(verticesToColor, vertices);
        verticesToColor.stream().forEach(System.out::println);

        // array to store which colors are available for current vertex
        boolean[] availableColors = new boolean[vertices.size() + 1];

        // iterate through verticesToColor, sorting inbetween each iteration (or just find max)
        // pick the vertex with the max number of adjacent colored vertices
        while(!verticesToColor.isEmpty()) {

            verticesToColor.sort((v1, v2) ->
               getNumAdjacentColored(v1) - getNumAdjacentColored(v2)
            );

            // set available colors
            Arrays.fill(availableColors, true);
            for(Vertex adjacent : verticesToColor.get(verticesToColor.size()-1).adj) {
                if(colors[adjacent.key] != 0) availableColors[colors[adjacent.key]] = false;
            }

            // find first available color
            int firstAvailable = 1;
            while(!availableColors[firstAvailable]) {
                firstAvailable++;
            }

            // set color of i to that
            colors[verticesToColor.get(verticesToColor.size()-1).key] = firstAvailable;

            verticesToColor.remove(verticesToColor.size()-1);
        }

        return colors;
    }

}
