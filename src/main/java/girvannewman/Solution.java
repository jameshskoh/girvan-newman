package girvannewman;

import girvannewman.data.EdgeData;
import graph.Graph;
import graph.VertexPair;

import java.util.*;

public class Solution {
    private final int numVert;
    private final int numEdge;

    private final Set<Integer>[] neighborsSet;
    private final Map<VertexPair, Integer> vertToEdge;
    private final EdgeData[] edgesData;

    private final List<Double> objList;
    private final List<Set<Integer>> killedEdgeList;

    public Solution(Graph graph) {
        this.neighborsSet = graph.exportGraph();

        numVert = graph.getNumVertex();
        numEdge = graph.getNumEdge();

        vertToEdge = new HashMap<>();
        edgesData = new EdgeData[numEdge];

        int edgeIndex = 0;
        for (int node = 0; node < numVert; node++) {
            Set<Integer> neighbors = neighborsSet[node];

            for (int neighbor : neighbors) {
                if(!hasEdge(node, neighbor)) {
                    addVertToEdge(edgeIndex, node, neighbor);
                    addEdgesData(edgeIndex, node, neighbor);
                    edgeIndex++;
                }
            }
        }

        objList = new ArrayList<>();
        objList.add(0.0);

        killedEdgeList = new ArrayList<>();
        killedEdgeList.add(new HashSet<>());
    }

    public void addVertToEdge(int edgeIndex, int node1, int node2){
        VertexPair vp = new VertexPair(node1, node2);
        vertToEdge.put(vp, edgeIndex);
    }

    public void addEdgesData(int edgeIndex, int node1, int node2) {
        edgesData[edgeIndex] = new EdgeData(edgeIndex, node1, node2);
    }

    public int getNumVert() {
        return numVert;
    }

    public int getNumEdge() {
        return numEdge;
    }

    public Set<Integer> getNeighbors(int index) {
        return neighborsSet[index];
    }

    public int getNumNeighbors(int index) {
        return getNeighbors(index).size();
    }

    public boolean areNeighbors(int node1, int node2) {
        return getNeighbors(node1).contains(node2);
    }

    public void addIter(Problem p) {
        addObj(p.getMod());
        addKilledEdge(p.getEdgesToKill());
    }

    private void addObj(double obj) {
        objList.add(obj);
    }

    public List<Double> getObjList() {
        return objList;
    }

    private void addKilledEdge(Set<Integer> killedEdges) {
        killedEdgeList.add(killedEdges);
    }

    public List<Set<Integer>> getKilledEdgeList() {
        return killedEdgeList;
    }

    public boolean hasEdge(int node1, int node2) {
        VertexPair vp = new VertexPair(node1, node2);
        return vertToEdge.containsKey(vp);
    }

    public int getEdge(int node1, int node2) {
        VertexPair vp = new VertexPair(node1, node2);
        return vertToEdge.get(vp);
    }

    public EdgeData getEdgeData(int node1, int node2) {
        VertexPair vp = new VertexPair(node1, node2);
        int edge = vertToEdge.get(vp);
        return edgesData[edge];
    }

    public EdgeData getEdgeData(int edge) {
        return edgesData[edge];
    }
}
