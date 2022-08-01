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
        if (neighborsSet.containsKey(num))
            return;

        Set<Integer> temp = new HashSet<>();
        neighborsSet.put(num, temp);
        numVertex++;
    }

    public void addEdge(int from, int to) {
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
