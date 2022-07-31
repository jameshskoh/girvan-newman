package girvannewman.data;

import girvannewman.Problem;
import girvannewman.Solution;

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
        flowCount = 1.0;
    }

    public void populateDownstream(Solution s, Problem p) {
        Map<Integer, FlowData> flowsData = p.getFlowsData();

        Set<Integer> nodeNeighbors = s.getNeighborsOf(node);

        for (int n : nodeNeighbors) {
            if (!upstream.contains(n)) {
                downstream.add(n);

                FlowData fd = new FlowData(n);
                flowsData.put(n, fd);
            }
        }
    }

    public void updateDownstream(Problem p) {
        for (int d : downstream) {
            FlowData downData = p.getFlowData(d);
            downData.incPathCount(pathCount);
            downData.addUpstream(node);
        }
    }

    private void incPathCount(int inc) {
        pathCount += inc;
    }

    private void addUpstream(int node) {
        upstream.add(node);
    }

    public void incUpstreamFlowAndBetw(Solution s, Problem p) {
        double proportion = calcProportion();

        for (int u : upstream) {
            FlowData upFlow = p.getFlowData(u);
            double inc = proportion * upFlow.pathCount;
            upFlow.incFlowCount(inc);
            p.incBetw(node, u, inc, s);
        }
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
}
