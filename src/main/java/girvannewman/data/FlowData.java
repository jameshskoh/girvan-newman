package girvannewman.data;

import graph.VertexPair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FlowData {
    private final int node;
    private final int level;
    private final Set<Integer> upstream;
    private final Set<Integer> downstream;
    private int pathCount;
    private double flowCount;

    public FlowData(int node, int level) {
        if (node < 0) {
            String msg = String.format(
                    "Node must be non-negative, received %d instead",
                    node);
            throw new IllegalArgumentException(msg);
        }

        if (level <= 0) {
            String msg = String.format(
                    "Level must be positive, received %d instead",
                    node);
            throw new IllegalArgumentException(msg);
        }

        this.node = node;
        this.level = level;
        upstream = new HashSet<>();
        downstream = new HashSet<>();
        pathCount = 0;
        flowCount = 0.0;
    }

    public void populateDownstream(
            Map<Integer, FlowData> flowsData, Set<Integer> nodeNeighbors,
            Set<VertexPair> severedPairs) {
        for (int n : nodeNeighbors) {
            VertexPair vp = new VertexPair(node, n);

            if (severedPairs.contains(vp)) continue;

            if (!flowsData.containsKey(n)) {
                downstream.add(n);
                FlowData fd = new FlowData(n, level + 1);
                flowsData.put(n, fd);
            } else if (flowsData.get(n).level == level + 1) {
                downstream.add(n);
            }
        }
    }

    public void updateDownstream(Map<Integer, FlowData> flowsData) {
        for (int d : downstream) {
            FlowData downData = flowsData.get(d);
            downData.incPathCount(pathCount);
            downData.addUpstream(node);
        }
    }

    public void incPathCount(int inc) {
        pathCount += inc;
    }

    private void addUpstream(int node) {
        upstream.add(node);
    }

    public Map<VertexPair, Double> incUpstreamFlow(Map<Integer, FlowData> flowsData) {
        Map<VertexPair, Double> betwInc = new HashMap<>();

        if (upstream.size() != 0) { // this is not root node, add 1 flow to self
            incFlowCount(1.0);
        } else {
            return betwInc;
        }

        double proportion = calcProportion();

        for (int u : upstream) {
            FlowData upFlow = flowsData.get(u);
            double inc = proportion * upFlow.pathCount;
            upFlow.incFlowCount(inc);

            betwInc.put(new VertexPair(node, u), inc);
        }

        return betwInc;
    }

    private double calcProportion() {
        return flowCount / pathCount;
    }

    private void incFlowCount(double inc) {
        flowCount += inc;
    }

    public int getNode() {
        return node;
    }

    public int getLevel() {
        return level;
    }

    public Set<Integer> getDownstream() {
        return downstream;
    }

    public Set<Integer> getUpstream() {
        return upstream;
    }

    public int getPathCount() {
        return pathCount;
    }

    public double getFlowCount() {
        return flowCount;
    }
}
