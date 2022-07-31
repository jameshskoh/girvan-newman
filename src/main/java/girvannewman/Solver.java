package girvannewman;

import girvannewman.data.FlowData;
import graph.Graph;

import java.util.*;

public class Solver {
    public Solution solve(Graph graph) {
        Solution s = new Solution(graph);

        double maxObj = 0;
        int maxObjIter = -1;

        double currObj = 10;
        int currIter = 0;
        int godMode = 0;
        int threshold = graph.getNumEdge() / 10;

        while(currObj > maxObj || godMode < threshold) {
            Problem p = new Problem(s, currIter);

            getEdgesWithHighBetw(s, p);
            killEdges(s, p);
            calcMod(s, p);
            currObj = p.getMod();

            if (maxObjIter <= currObj) {
                maxObjIter = currIter;
                maxObj = currObj;
            } else {
                godMode++;
            }

            s.addIter(p);

            currIter++;
        }

        return s;
    }

    public void getEdgesWithHighBetw(Solution s, Problem p) {
        calcBetw(s, p);

        double[] currBetw = p.getBetw();
        double max = 0;
        for (double v : currBetw) {
            if (max < v) {
                max = v;
            }
        }

        Set<Integer> edgesToKill = new HashSet<>();
        for (int edge = 0; edge < currBetw.length; edge++) {
            if (currBetw[edge] == max) {
                edgesToKill.add(edge);
            }
        }

        p.setEdgesToKill(edgesToKill);
    }

    private void calcBetw(Solution s, Problem p) {
        for (int vert = 0; vert < s.getNumVert(); vert++) {
            incBetwViaBFS(vert, s, p);
        }
    }

    private void incBetwViaBFS(int start, Solution s, Problem p) {
        p.resetFlowsData();
        Map<Integer, FlowData> flowsData = p.getFlowsData();

        FlowData flowSrc = new FlowData(start);
        Queue<FlowData> pathQueue = new LinkedList<>();
        pathQueue.add(flowSrc);

        Stack<FlowData> flowStack = new Stack<>();

        while (!pathQueue.isEmpty()) {
            FlowData curr = pathQueue.poll();
            flowStack.add(curr);

            curr.populateDownstream(s, p);
            curr.incDownstreamPaths(p);

            for (int d : curr.getDownstream()) {
                pathQueue.add(flowsData.get(d));
            }
        }

        while (!flowStack.isEmpty()) {
            FlowData flowData = flowStack.pop();
            flowData.incUpstreamFlow(s, p);
        }
    }

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

                    Set<Integer> neighbors = s.getNeighbors(currNode);
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
        val -= (s.getNumNeighbors(node1) * s.getNumNeighbors(node2)) / (2.0 * m);
        val /= (2.0 * m);

        p.incMod(val);
    }
}
