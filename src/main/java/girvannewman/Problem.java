package girvannewman;

import girvannewman.data.FlowData;

import java.util.*;

public class Problem {
    private final int iter;
    private final double[] betw;
    private Map<Integer, FlowData> flowsData;
    private Set<Integer> edgesToKill;
    private final Map<Integer, Integer> vertCommunity;
    private final Map<Integer, Set<Integer>> communitiesSet;
    private double mod;


    public Problem(Solution s, int iter) {
        this.iter = iter;

        betw = new double[s.getNumEdge()];
        Arrays.fill(betw, -1);

        vertCommunity = new HashMap<>();
        communitiesSet = new HashMap<>();
    }

    public int getIter() {
        return iter;
    }

    public double[] getBetw() {
        return betw;
    }

    public void incBetw(int node1, int node2, double inc, Solution s) {
        int edge = s.getEdge(node1, node2);
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

    public boolean isInComm(int node) {
        return vertCommunity.containsKey(node);
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
