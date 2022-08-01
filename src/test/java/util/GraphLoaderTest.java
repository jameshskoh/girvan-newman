package util;

import graph.Graph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class GraphLoaderTest {
    static Stream<Arguments> values() {
        return Stream.of(
                arguments("data/dolphins.edge", 62, 159),
                arguments("data/karate.edge", 34, 78),
                arguments("data/facebook_ucsd.edge", 14948, 443221)
        );
    }

    @ParameterizedTest
    @MethodSource("values")
    void loadGraph_shouldHaveCorrectNumbers(String file, int numVert, int numEdge) {
        Graph g = new Graph();

        try {
            GraphLoader.loadGraph(g, file);

            assertEquals(numVert, g.getNumVertex());
            assertEquals(numEdge, g.getNumEdge());

            Map<Integer, Set<Integer>> n = g.exportGraph();
            assertEquals(numVert, n.size());

            int total = 0;

            for (Set<Integer> s : n.values()) {
                total += s.size();
            }

            assertEquals(numEdge, total / 2);
        } catch (Exception e) {
            fail("Should not throw exception.");
        }
    }

    @Test
    void loadGraph_missingFileShouldThrowFNFE() {
        Graph g = new Graph();

        assertThrows(FileNotFoundException.class,
                () -> {
                    GraphLoader.loadGraph(g, "nonexistent.edge");
                });
    }
}