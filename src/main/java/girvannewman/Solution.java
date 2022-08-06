package girvannewman;

import girvannewman.data.EdgeData;
import graph.Graph;
import graph.VertexPair;

import java.util.*;

public class Solution {
    private final Map<Integer, Set<Integer>> neighborsSet;
    private final int numVert;
    private final int numEdge;
    private final Map<VertexPair, Integer> vertToEdge;
    private final EdgeData[] edgesData;

    private final List<Double> objList;
    private int optIter;

    private final Set<VertexPair> severedPairs;
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
            Set<Integer> neighbors = neighborsSet.get(node);

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

        severedPairs = new HashSet<>();
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
    //</editor-fold>

    public Set<Integer> getNeighborsOf(int node) {
        if (!hasNode(node)) {
            String msg = String.format("Node %d does not exist.", node);
            throw new IllegalArgumentException(msg);
        }

        return neighborsSet.get(node);
    }

    public int getNumNeighborsOf(int node) {
        return getNeighborsOf(node).size();
    }

    public boolean areNeighbors(int node1, int node2) {
        if (!hasNode(node1)) {
            return false;
        }

        return getNeighborsOf(node1).contains(node2);
    }

    private boolean hasNode(int node) {
        return neighborsSet.containsKey(node);
    }

    public boolean hasEdge(int node1, int node2) {
        VertexPair vp = new VertexPair(node1, node2);
        return vertToEdge.containsKey(vp);
    }

    public int getEdge(VertexPair vp) {
        return vertToEdge.get(vp);
    }

    public EdgeData getEdgeData(int edge) {
        return edgesData[edge];
    }

    public EdgeData getEdgeData(int node1, int node2) {
        VertexPair vp = new VertexPair(node1, node2);
        int edge = getEdge(vp);
        return edgesData[edge];
    }

    public int getNumVert() {
        return numVert;
    }

    public int getNumEdge() {
        return numEdge;
    }

    //<editor-fold desc="End of iteration methods">
    public void addResultAndSetOpt(double obj, Set<Integer> killedEdges) {
        if (killedEdges == null) {
            throw new NullPointerException("Killed edges set is null.");
        }

        objList.add(obj);

        if (obj >= objList.get(optIter)) {
            optIter = objList.size() - 1;
        }

        addKilledEdge(killedEdges);
    }

    public void setOptCommSets(Map<Integer, Set<Integer>> optCommSet) {
        if (optCommSet == null) {
            throw new NullPointerException("Optimal communities set is null.");
        }

        optCommunitiesSet = optCommSet;
    }

    public Map<Integer, Set<Integer>> getOptCommSets() {
        return optCommunitiesSet;
    }

    public List<Double> getObjList() {
        return objList;
    }

    public int getOptIter() {
        return optIter;
    }

    public void addSeveredPairs(VertexPair vp) {
        severedPairs.add(vp);
    }

    public Set<VertexPair> getSeveredPairs() {
        return severedPairs;
    }

    private void addKilledEdge(Set<Integer> killedEdges) {
        killedEdgeList.add(killedEdges);
    }

    public List<Set<Integer>> getKilledEdgeList() {
        return killedEdgeList;
    }
    //</editor-fold>
}
