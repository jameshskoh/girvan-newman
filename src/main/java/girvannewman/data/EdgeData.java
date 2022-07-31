package girvannewman.data;

import graph.VertexPair;

public class EdgeData {
    private final int edgeIndex;
    private final VertexPair endpoints;

    private boolean alive;
    private int iterKilled;

    public EdgeData(int edgeIndex, int node1, int node2) {
        // node order, node equal throw

        this.edgeIndex = edgeIndex;
        endpoints = new VertexPair(node1, node2);

        alive = true;
        iterKilled = -1;
    }

    public void kill(int iter) {
        // iter nonpositive throw

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
