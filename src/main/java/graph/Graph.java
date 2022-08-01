package graph;

import java.util.*;

public class Graph {
    private int numVertex;
    private int numEdge;
    private final Map<Integer, Set<Integer>> neighborsSet;

    public Graph() {
        neighborsSet = new HashMap<>();
    }

    public void addVertex(int num) {
        if (num < 0) {
            String msg = String.format("Node index cannot be negative, %d received instead.", num);
            throw new IllegalArgumentException(msg);
        }

        if (neighborsSet.containsKey(num))
            return;

        Set<Integer> temp = new HashSet<>();
        neighborsSet.put(num, temp);
        numVertex++;
    }

    public void addEdge(int from, int to) {
        if (from < 0 || to < 0) {
            String msg = String.format("Node index cannot be negative, %d and %d received instead.", from, to);
            throw new IllegalArgumentException(msg);
        }

        if (from == to) {
            String msg = String.format("From node and to node must not be the same, %d received instead.", from );
            throw new IllegalArgumentException(msg);
        }

        if (neighborsSet.get(from).contains(to))
            return;

        Set<Integer> fromNeigh = neighborsSet.get(from);
        fromNeigh.add(to);

        Set<Integer> toNeigh = neighborsSet.get(to);
        toNeigh.add(from);

        numEdge++;
    }

    public int getNumVertex() {
        return numVertex;
    }

    public int getNumEdge() {
        return numEdge;
    }

    public Map<Integer, Set<Integer>> exportGraph() {
        return neighborsSet;
    }
}
