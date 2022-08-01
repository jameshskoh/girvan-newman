package girvannewman.data;

import graph.VertexPair;

public class EdgeData {
    private final int edgeIndex;
    private final VertexPair endpoints;

    private boolean alive;
    private int iterKilled;

    public EdgeData(int node1, int node2, int edgeIndex) {
        if (node1 < 0 || node2 < 0 || edgeIndex < 0) {
            String msg = String.format("Indices must be non-negative, received %d, %d, %d instead.",
                    node1, node2, edgeIndex);
            throw new IllegalArgumentException(msg);
        }

        if (node1 == node2) {
            String msg = String.format("Node index must not be equal, received %d instead.",
                    node1);
            throw new IllegalArgumentException(msg);
        }

        this.edgeIndex = edgeIndex;
        endpoints = new VertexPair(node1, node2);

        alive = true;
        iterKilled = -1;
    }

    public void kill(int iter) {
        if (iter <= 0) {
            String msg = String.format("Iteration must be non-negative, received %d instead.",
                    iter);
            throw new IllegalArgumentException(msg);
        }

        alive = false;
        iterKilled = iter;
    }

    public int getEdgeIndex() {
        return edgeIndex;
    }

    public VertexPair getEndpoints() {
        return endpoints;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getIterKilled() {
        return iterKilled;
    }
}
