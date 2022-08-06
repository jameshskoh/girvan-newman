import girvannewman.Solution;
import girvannewman.Solver;
import graph.Graph;
import util.GraphLoader;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        System.out.println("App started.");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Graph g = new Graph();
        try {
            GraphLoader.loadGraph(g, "data/facebook_ucsd_1500.edge");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Graph loaded.");

        Solver sv = new Solver();
        Solution s = sv.solve(g, g.getNumEdge()/1000);

        Map<Integer, Set<Integer>> commSets = s.getOptCommSets();

        String msg = String.format("\nObjective \t %f \t # of communities \t %d \n", s.getObjList().get(s.getOptIter()), s.getOptCommSets().size());
        System.out.println(msg);

        for (int set : commSets.keySet()) {
            String msg1 = String.format("Set %d", set + 1);
            System.out.println(msg1);

            int count = 0;
            for (int node : commSets.get(set)) {
                System.out.print(node + "\t");
                count++;

                if (count % 10 == 0) {
                    System.out.println();
                }
            }

            System.out.println();
            System.out.println();
        }
    }
}
