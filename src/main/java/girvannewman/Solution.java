package girvannewman;

import girvannewman.data.EdgeData;
import graph.Graph;
import graph.VertexPair;

import java.util.*;

public class Solution {
    private final Set<Integer>[] neighborsSet;
    private final int numVert;
    private final int numEdge;
    private final Map<VertexPair, Integer> vertToEdge;
    private final EdgeData[] edgesData;

    private final List<Double> objList;
    private int optIter;
    private final List<Set<Integer>> killedEdgeList;
    private Map<Integer, Set<Integer>> optCommunitiesSet;

    public Solution(Graph graph) {
        neighborsSet = graph.exportGraph();
        numVert = graph.getNumVertex();
        numEdge = graph.getNumEdge();

        vertToEdge = new HashMap<>();
        edgesData = new EdgeData[numEdge];

        int edge = 0;
        for (int node = 0; node < numVert; node++) {
            Set<Integer> neighbors = neighborsSet[node];

            for (int neighbor : neighbors) {
                if(!hasEdge(node, neighbor)) {
                    addVertToEdge(node, neighbor, edge);
                    addEdgesData(node, neighbor, edge);
                    edge++;
                }
            }
        }

        objList = new ArrayList<>();
        objList.add(0.0);

        killedEdgeList = new ArrayList<>();
        killedEdgeList.add(new HashSet<>());
    }

    //<editor-fold desc="Constructor helpers">
    private void addVertToEdge(int node1, int node2, int edgeIndex){
        VertexPair vp = new VertexPair(node1, node2);
        vertToEdge.put(vp, edgeIndex);
    }

    private void addEdgesData(int node1, int node2, int edgeIndex) {
        edgesData[edgeIndex] = new EdgeData(node1, node2, edgeIndex);
    }

    public boolean hasEdge(int node1, int node2) {
        VertexPair vp = new VertexPair(node1, node2);
        return vertToEdge.containsKey(vp);
    }
    //</editor-fold>

    public int getEdge(int node1, int node2) {
        VertexPair vp = new VertexPair(node1, node2);
        return vertToEdge.get(vp);
    }

    public EdgeData getEdgeData(int edge) {
        return edgesData[edge];
    }

    public EdgeData getEdgeData(int node1, int node2) {
        VertexPair vp = new VertexPair(node1, node2);
        int edge = vertToEdge.get(vp);
        return edgesData[edge];
    }

    public int getNumVert() {
        return numVert;
    }

    public int getNumEdge() {
        return numEdge;
    }

    public Set<Integer> getNeighborsOf(int node) {
        return neighborsSet[node];
    }

    public int getNumNeighborsOf(int node) {
        return getNeighborsOf(node).size();
    }

    public boolean areNeighbors(int node1, int node2) {
        return getNeighborsOf(node1).contains(node2);
    }

    //<editor-fold desc="End of iteration methods">
    public void addResult(Problem p, int maxObjIter) {
        addObj(p.getMod());
        addKilledEdge(p.getEdgesToKill());
        setOptIter(maxObjIter);
    }

    private void addObj(double obj) {
        objList.add(obj);
    }

    private void addKilledEdge(Set<Integer> killedEdges) {
        killedEdgeList.add(killedEdges);
    }

    private void setOptIter(int optIter) {
        this.optIter = optIter;
    }

    public void setOptCommunitiesSet(Map<Integer, Set<Integer>> optCommunitiesSet) {
        this.optCommunitiesSet = optCommunitiesSet;
    }

    //</editor-fold>

    //<editor-fold desc="Result getters">
    public List<Double> getObjList() {
        return objList;
    }

    public int getOptIter() {
        return optIter;
    }

    public List<Set<Integer>> getKilledEdgeList() {
        return killedEdgeList;
    }

    public Map<Integer, Set<Integer>> getOptCommunitiesSet() {
        return optCommunitiesSet;
    }

    //</editor-fold>

}
