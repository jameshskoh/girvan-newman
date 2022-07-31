package graph;

import java.util.Objects;

public class VertexPair {
    private int vert1;
    private int vert2;

    public VertexPair(int vert1, int vert2) {
        this.vert1 = vert1;
        this.vert2 = vert2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexPair that = (VertexPair) o;
        return vert1 == that.vert1 && vert2 == that.vert2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vert1, vert2);
    }
}
