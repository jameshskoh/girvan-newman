package girvannewman;

import girvannewman.data.EdgeData;
import girvannewman.data.FlowData;
import graph.Graph;
import graph.VertexPair;

import java.util.*;

public class Solver {
    public Solution solve(Graph graph, int threshold) {
        System.out.println("Solver started");

        Solution s = new Solution(graph);

        double optObj = Double.NEGATIVE_INFINITY;
        double currObj = 0;
        int currIter = 1;
        int godMode = 0;

        System.out.println("Iteration started.");

        while (currObj >= optObj || godMode < threshold) {
            if (currIter % 10 == 0) {
                String msg = String.format("Now on iteration %d \t obj = %f \t opt = %f", currIter, currObj, optObj);
                System.out.println(msg);
            }

            Problem p = new Problem(s.getNumEdge(), currIter);

            getEdgesToKill(s, p);
            killEdges(s, p);
            calcMod(s, p);
            currObj = p.getMod();

            s.addResultAndSetOpt(p.getMod(), p.getEdgesToKill());

            if (optObj <= currObj) {
                optObj = currObj;
                s.setOptCommSets(p.getCommunitiesSet());
            } else {
                godMode++;
            }

            currIter++;
        }

        return s;
    }

    //<editor-fold desc="Calculate betweenness">
    public void getEdgesToKill(Solution s, Problem p) {
        calcBetw(s, p);

        double max = 0;
        for (int edge = 0; edge < s.getNumEdge(); edge++) {
            double currBetw = p.getBetw(edge);

            if (max < currBetw) {
                max = currBetw;
            }
        }

        Set<Integer> edgesToKill = new HashSet<>();
        for (int edge = 0; edge < s.getNumEdge(); edge++) {
            if (p.getBetw(edge) == max) {
                edgesToKill.add(edge);
            }
        }

        p.setEdgesToKill(edgesToKill);
    }

    private void calcBetw(Solution s, Problem p) {
        for (int node = 0; node < s.getNumVert(); node++) {
            incFlowAndBetwViaBFS(node, s, p);
        }
    }

    private void incFlowAndBetwViaBFS(int src, Solution s, Problem p) {
        p.resetFlowsData();

        FlowData flowSrc = new FlowData(src, 1);
        flowSrc.incPathCount(1);

        Map<Integer, FlowData> flowsData = p.getFlowsData();
        flowsData.put(src, flowSrc);

        Queue<FlowData> pathQueue = new LinkedList<>();
        pathQueue.add(flowSrc);

        Set<Integer> visited = new HashSet<>();

        Stack<FlowData> flowStack = new Stack<>();

        while (!pathQueue.isEmpty()) {
            FlowData curr = pathQueue.poll();

            if (!visited.contains(curr.getNode())) {
                visited.add(curr.getNode());
            } else {
                continue;
            }

            flowStack.add(curr);

            Set<Integer> nodeNeighbors = s.getNeighborsOf(curr.getNode());

            curr.populateDownstream(flowsData, nodeNeighbors, s.getSeveredPairs());
            curr.updateDownstream(flowsData);

            for (int d : curr.getDownstream()) {
                pathQueue.add(p.getFlowData(d));
            }
        }

        while (!flowStack.isEmpty()) {
            FlowData flowData = flowStack.pop();

            Map<VertexPair, Double> betwInc = flowData.incUpstreamFlow(flowsData);

            for (VertexPair vp : betwInc.keySet()) {
                int edge = s.getEdge(vp);
                p.incBetw(edge, betwInc.get(vp));
            }
        }
    }
    //</editor-fold>

    public void killEdges(Solution s, Problem p) {
        Set<Integer> edgesToKill = p.getEdgesToKill();

        for (int edge : edgesToKill) {
            EdgeData ed = s.getEdgeData(edge);
            VertexPair vp = ed.getEndpoints();

            s.getEdgeData(edge).kill(p.getIter());
            s.addSeveredPairs(vp);
        }
    }

    public void calcMod(Solution s, Problem p) {
        findCommSet(s, p);

        for (Set<Integer> comm : p.getCommunitiesSet().values()) {
            for (int node : comm) {
                for (int friend : comm) {
                    calcModIter(node, friend, s, p);
                }
            }
        }
    }

    private void findCommSet(Solution s, Problem p) {
        for (int node = 0; node < s.getNumVert(); node++) {
            if (!p.isInComm(node)) {
                HashSet<Integer> visited = new HashSet<>();
                int currComm = p.initNewComm();

                Queue<Integer> bfsQueue = new LinkedList<>();
                bfsQueue.add(node);

                while (!bfsQueue.isEmpty()) {
                    int currNode = bfsQueue.poll();
                    visited.add(currNode);

                    p.putInComm(currComm, currNode);

                    Set<Integer> neighbors = s.getNeighborsOf(currNode);
                    for (int n : neighbors) {
                        if (!visited.contains(n)
                                && s.getEdgeData(currNode, n).isAlive()) {
                            bfsQueue.add(n);
                        }
                    }
                }
            }
        }
    }

    private void calcModIter(int node1, int node2, Solution s, Problem p) {
        if (node1 == node2) {
            return;
        }

        int m = s.getNumEdge();

        double val = s.areNeighbors(node1, node2) ? 1.0 : 0.0;
        val -= (s.getNumNeighborsOf(node1) * s.getNumNeighborsOf(node2)) / (2.0 * m);
        val /= (2.0 * m);

        p.incMod(val);
    }
}
