package graph;

import java.util.Objects;

public class VertexPair {
    private final int vert1;
    private final int vert2;

    public VertexPair(int vert1, int vert2) {
        this.vert1 = vert1;
        this.vert2 = vert2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexPair that = (VertexPair) o;
        return (vert1 == that.vert1 && vert2 == that.vert2)
                || (vert1 == that.vert2 && vert2 == that.vert1);
    }

    @Override
    public int hashCode() {
        int res = Math.max(vert1, vert2);
        res = (res << 16) | (res >>> 16);  // exchange top and bottom 16 bits.
        res = res ^ Math.min(vert1, vert2);
        return res;
    }
}
