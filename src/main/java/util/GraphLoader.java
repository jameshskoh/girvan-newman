package util;

import graph.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class GraphLoader {
    public static void loadGraph(Graph g, String filename) throws FileNotFoundException {
        Set<Integer> seen = new HashSet<>();
        Scanner sc;

        sc = new Scanner(new File(filename));

        while (sc.hasNextInt()) {
            int v1 = sc.nextInt();
            int v2 = sc.nextInt();

            if (!seen.contains(v1)) {
                g.addVertex(v1);
                seen.add(v1);
            }

            if (!seen.contains(v2)) {
                g.addVertex(v2);
                seen.add(v2);
            }

            g.addEdge(v1, v2);
        }

        sc.close();
    }
}
