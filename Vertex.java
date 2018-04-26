import java.util.ArrayList;

public class Vertex {
    int key;
    ArrayList<Vertex> adj;
    public Vertex(int k) {
        this.key = k;
        adj = new ArrayList<>();
    }

    public boolean equals(Object o) {
        Vertex v = (Vertex) o;
        return v.key == this.key;
    }

    public String toString() {
        return key + "";
    }
}
