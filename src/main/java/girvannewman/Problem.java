package girvannewman;

import girvannewman.data.FlowData;

import java.util.*;

public class Problem {
    private final int iter;
    private final double[] betw;
    private final Map<Integer, Integer> vertCommunity;
    private final Map<Integer, Set<Integer>> communitiesSet;
    private Map<Integer, FlowData> flowsData;
    private Set<Integer> edgesToKill;
    private double mod;

    public Problem(int numEdge, int iter) {
        if (numEdge < 0) {
            String msg = String.format("Number of edges must be non-negative, received %d instead.", numEdge);
            throw new IllegalArgumentException(msg);
        }

        if (iter <= 0) {
            String msg = String.format("Iteration must be positive, received %d instead.", iter);
            throw new IllegalArgumentException(msg);
        }

        this.iter = iter;

        betw = new double[numEdge];

        vertCommunity = new HashMap<>();
        communitiesSet = new HashMap<>();
        resetFlowsData();
    }

    public int getIter() {
        return iter;
    }

    public double getBetw(int edge) {
        return betw[edge];
    }

    public void incBetw(int edge, double inc) {
        if (inc < 0.0) {
            String msg = String.format("Betweenness increment must be non-negative, received %f instead.", inc);
            throw new IllegalArgumentException(msg);
        }

        if (edge < 0 || edge >= betw.length) {
            String msg = String.format("Invalid edge index %d.", edge);
            throw new IllegalArgumentException(msg);
        }

        betw[edge] += inc;
    }

    public Set<Integer> getEdgesToKill() {
        return edgesToKill;
    }

    public void setEdgesToKill(Set<Integer> edges) {
        edgesToKill = edges;
    }

    public void resetFlowsData() {
        flowsData = new HashMap<>();
    }

    public Map<Integer, FlowData> getFlowsData() {
        return flowsData;
    }

    public FlowData getFlowData(int node) {
        if (!flowsData.containsKey(node)) {
            String msg = String.format("Element %d not found", node);
            throw new NoSuchElementException(msg);
        }

        return flowsData.get(node);
    }

    public int initNewComm() {
        int index = communitiesSet.size();
        communitiesSet.put(index, new HashSet<>());

        return index;
    }

    public void putInComm(int commIndex, int node) {
        vertCommunity.put(node, commIndex);
        Set<Integer> set = communitiesSet.get(commIndex);
        set.add(node);
        communitiesSet.put(commIndex, set);
    }

    public boolean isInComm(int node) {
        return vertCommunity.containsKey(node);
    }

    public Map<Integer, Set<Integer>> getCommunitiesSet() {
        return communitiesSet;
    }

    public void incMod(double inc) {
        mod += inc;
    }

    public double getMod() {
        return mod;
    }
}
