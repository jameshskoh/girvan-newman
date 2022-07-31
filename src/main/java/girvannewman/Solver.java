package girvannewman;

import girvannewman.data.FlowData;
import graph.Graph;

import java.util.*;

public class Solver {
    public Solution solve(Graph graph) {
        Solution s = new Solution(graph);

        double optObj = Double.NEGATIVE_INFINITY;
        int optIter = -1;
        double currObj = 0;
        int currIter = 0;
        int godMode = 0;
        int threshold = graph.getNumEdge() / 10;

        while(currObj > optObj || godMode < threshold) {
            Problem p = new Problem(s, currIter);

            getEdgesToKill(s, p);
            killEdges(s, p);
            calcMod(s, p);
            currObj = p.getMod();

            if (optIter <= currObj) {
                optIter = currIter;
                optObj = currObj;
                s.setOptCommunitiesSet(p.getCommunitiesSet());
            } else {
                godMode++;
            }

            s.addResult(p, optIter);

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
        FlowData flowSrc = new FlowData(src);

        Queue<FlowData> pathQueue = new LinkedList<>();
        pathQueue.add(flowSrc);

        Stack<FlowData> flowStack = new Stack<>();

        while (!pathQueue.isEmpty()) {
            FlowData curr = pathQueue.poll();
            flowStack.add(curr);

            curr.populateDownstream(s, p);
            curr.updateDownstream(p);

            for (int d : curr.getDownstream()) {
                pathQueue.add(p.getFlowData(d));
            }
        }

        while (!flowStack.isEmpty()) {
            FlowData flowData = flowStack.pop();
            flowData.incUpstreamFlowAndBetw(s, p);
        }
    }
    //</editor-fold>

    public void killEdges(Solution s, Problem p) {
        Set<Integer> edgesToKill = p.getEdgesToKill();

        for (int edge : edgesToKill) {
            s.getEdgeData(edge).kill(p.getIter());
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
        int m = s.getNumEdge();

        double val = s.areNeighbors(node1, node2) ? 1.0 : 0.0;
        val -= (s.getNumNeighborsOf(node1) * s.getNumNeighborsOf(node2)) / (2.0 * m);
        val /= (2.0 * m);

        p.incMod(val);
    }
}
