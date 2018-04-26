import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class ColorableGraph {


    ArrayList<Vertex> vertices;
    ArrayList<Vertex> useThisToFindVertexByKey;
    boolean[] preColored;
    int[] colors;

    public ColorableGraph(int size, int[] colors) {

        // init adjacency list and sorting list
        vertices = new ArrayList<>();
        useThisToFindVertexByKey = new ArrayList<>();
        for(int i = 0; i < size; i++) { vertices.add(new Vertex(i+1)); useThisToFindVertexByKey.add(new Vertex(i+1)); }

        this.preColored = new boolean[size+1];
        for(int i = 1; i <= size; i++) {
            preColored[i] = (colors[i] != 0);
        }

        // init colors
        this.colors = colors;

    }

    public ColorableGraph(int size) {
        this(size, new int[size+1]); // we will use the key to store color, so since keys start at 1, we need size+1 spots if starting at 0
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

        // Welsh-Powell Algorithm
        ArrayList<Vertex> verticesToColor = new ArrayList<>();
        for(Vertex v : vertices) {
            if(colors[v.key] == 0) {
                verticesToColor.add(v);
            }
        }

        // sort the array of verticesToColor
        Collections.sort(verticesToColor, (u, v) -> v.adj.size() - u.adj.size());
        verticesToColor.stream().forEach(System.out::println);

        // look for color of each vertex
        boolean[] availableColors = new boolean[vertices.size() + 1];
        for(int i = 0; i < verticesToColor.size(); i++) {

            // set available colors for current vertex in list
            Arrays.fill(availableColors, true);
            for(Vertex adjacent : verticesToColor.get(i).adj) {
                if(colors[adjacent.key] != 0) availableColors[colors[adjacent.key]] = false;
            }

            // find first available color
            int firstAvailable = 1;
            while(!availableColors[firstAvailable]) {
                firstAvailable++;
            }

            // set color of i to that
            colors[verticesToColor.get(i).key] = firstAvailable;
        }

        return colors;
    }

    public int[] fixBySwap() {

        // init start time, for tracking time passed
        long startTime = System.currentTimeMillis();


        Random random = new Random();
        boolean[] availableColorsCurrent = new boolean[vertices.size()];
        int count = 0;
        boolean doneWithThisMaxVertex = false;
        boolean swappedRecently = true;
        boolean nextColorAvailable = true;

        ArrayList<Vertex> maxVertices = new ArrayList<>();

        int sometimesDoRandom = 0;
        // repeatedly find max vertex, and reduce, until out of time
        while(true) {
            if(System.currentTimeMillis() - startTime > 20000) break;

            //System.out.println(preColored[21] + " color: " + colors[21]);

            // find vertex with max color
            Vertex currentVertex;
            if(sometimesDoRandom%10 < 6) {
                maxVertices.clear();
                maxVertices.add(vertices.get(0));
                for (int i = 1; i < vertices.size(); i++) {
                    if (colors[vertices.get(i).key] == colors[maxVertices.get(0).key]) maxVertices.add(vertices.get(i));
                    if (colors[vertices.get(i).key] > colors[maxVertices.get(0).key]) {
                        maxVertices.clear();
                        maxVertices.add(vertices.get(i));
                    }
                }
                currentVertex = maxVertices.get(random.nextInt(maxVertices.size()));
            } else if(sometimesDoRandom %10 < 7){
                currentVertex = vertices.get(random.nextInt(vertices.size()));
            } else {
                Collections.sort(vertices, (v1, v2)-> {
                    return colors[v1.key] - colors[v2.key];
                });
                int index = random.nextInt(1+random.nextInt(1+random.nextInt(1+random.nextInt(vertices.size()))));
                //System.out.println("index: " + index);
                currentVertex = vertices.get(index);
            }
            sometimesDoRandom++;

            System.out.println(currentVertex.key);

            //System.out.println(currentVertex.key);

            //System.out.println(currentVertex.key);
            /*for(Vertex v : currentVertex.adj) {
                System.out.print(v.key + " ");
            }
            System.out.println();*/

            // attempt to swap max vertex around until we can reduce, or count goes over 1000
            count = 0;
            while (count < 15) {
                //System.out.println("inside");

                if(preColored[currentVertex.key]) {
                    break;
                }

                // find available colors for current vertex
                Arrays.fill(availableColorsCurrent, true);
                for (Vertex adjacent : currentVertex.adj) {
                    if (colors[adjacent.key] != 0) availableColorsCurrent[colors[adjacent.key]] = false;
                }
                // if a lower color is available, switch to it and break
                doneWithThisMaxVertex = false;
                for (int i = 1; i < colors[currentVertex.key]; i++) {
                    if (availableColorsCurrent[i] == true) {
                        colors[currentVertex.key] = i;
                        System.out.println("reducing!");
                        System.out.println("new color: " + colors[currentVertex.key]);
                        doneWithThisMaxVertex = true;
                        break;
                    }
                }
                if (doneWithThisMaxVertex) break;

                // okay this part will be the whole rest of the method
                // try to swap with one random next vertex
                nextColorAvailable = true;
                int adjIndex = random.nextInt(currentVertex.adj.size());
                Vertex nextVertex = currentVertex.adj.get(adjIndex);
                //if(nextVertex.key == 34) System.out.println("found 34");
                /*System.out.println(adjIndex);
                for(Vertex v : currentVertex.adj) {
                    System.out.print(v.key + " ");
                }
                System.out.println();*/
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
                    if(nextColorAvailable && !preColored[nextVertex.key]) {
                        // now both slots are available for swapping
                        System.out.println("Swapping: " + currentVertex.key + " color: " + colors[currentVertex.key] + "with: " + nextVertex.key + " color: " + colors[nextVertex.key]);
                        int temp = colors[currentVertex.key];
                        colors[currentVertex.key] = colors[nextVertex.key];
                        colors[nextVertex.key] = temp;

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
