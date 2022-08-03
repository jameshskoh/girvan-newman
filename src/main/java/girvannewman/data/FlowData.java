package girvannewman.data;

import graph.VertexPair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FlowData {
    private final int node;
    private final Set<Integer> upstream;
    private final Set<Integer> downstream;
    private int pathCount;
    private double flowCount;

    public FlowData(int node) {
        this.node = node;
        upstream = new HashSet<>();
        downstream = new HashSet<>();
        pathCount = 0;
        flowCount = 0.0;
    }

    public void populateDownstream(Map<Integer, FlowData> flowsData, Set<Integer> nodeNeighbors) {
        for (int n : nodeNeighbors) {
            if (!upstream.contains(n)) {
                downstream.add(n);

                FlowData fd = new FlowData(n);

                if (!flowsData.containsKey(n)) {
                    flowsData.put(n, fd);
                }
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
