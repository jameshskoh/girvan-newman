package girvannewman;

import graph.Graph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.GraphLoader;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static util.Constants.EPSILON;

class SolverTest {
    static Stream<Arguments> small1() {
        Graph g = new Graph();
        try {
            GraphLoader.loadGraph(g, "data/small1.edge");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("Should not throw FNFE.");
        }

        Set<Integer> edgesToKill = Stream.of(3, 4).collect(Collectors.toSet());

        Map<Integer, Set<Integer>> optCommSets = Map.of(
                0, Stream.of(0, 1, 2, 3).collect(Collectors.toSet()),
                1, Stream.of(4, 5, 6, 7).collect(Collectors.toSet())
        );

        return Stream.of(
                Arguments.arguments(g, edgesToKill, optCommSets, 33.0 / 81.0)
        );
    }

    @ParameterizedTest
    @MethodSource("small1")
    void getEdgesToKill_shouldGiveCorrectEdges(Graph g, Set<Integer> edgesToKill,
                                               Map<Integer, Set<Integer>> optCommSets, double mod) {
        Solver sv = new Solver();
        Solution s = new Solution(g);
        Problem p = new Problem(s.getNumEdge(), 1);

        sv.getEdgesToKill(s, p);

        Set<Integer> myEdgesToKill = p.getEdgesToKill();
        assertEquals(edgesToKill.size(), myEdgesToKill.size());

        for (int edge = 0; edge < g.getNumEdge(); edge++) {
            assertEquals(edgesToKill.contains(edge), myEdgesToKill.contains(edge));
        }

        assertFalse(myEdgesToKill.contains(-1));
        assertFalse(myEdgesToKill.contains(g.getNumEdge() + 1));
        assertFalse(myEdgesToKill.contains(g.getNumEdge() + 2));
        assertFalse(myEdgesToKill.contains(g.getNumEdge() + 10));
    }

    @ParameterizedTest
    @MethodSource("small1")
    void killEdges_shouldKillCorrectEdges(Graph g, Set<Integer> edgesToKill,
                                          Map<Integer, Set<Integer>> optCommSets, double mod) {
        Solver sv = new Solver();
        Solution s = new Solution(g);
        Problem p = new Problem(s.getNumEdge(), 1);

        sv.getEdgesToKill(s, p);
        sv.killEdges(s, p);

        for (int edge = 0; edge < g.getNumEdge(); edge++) {
            assertEquals(edgesToKill.contains(edge), !s.getEdgeData(edge).isAlive());
        }
    }

    @ParameterizedTest
    @MethodSource("small1")
    void calcMod_shouldCreateCorrectComm(Graph g, Set<Integer> edgesToKill,
                                         Map<Integer, Set<Integer>> optCommSets, double mod) {
        Solver sv = new Solver();
        Solution s = new Solution(g);
        Problem p = new Problem(s.getNumEdge(), 1);

        sv.getEdgesToKill(s, p);
        sv.killEdges(s, p);
        sv.calcMod(s, p);

        Map<Integer, Set<Integer>> myOptCommSets = p.getCommunitiesSet();

        for (int iter = 0; iter < optCommSets.size(); iter++) {
            Set<Integer> set = optCommSets.get(iter);
            Set<Integer> mySet = myOptCommSets.get(iter);

            for (int node = 0; node < g.getNumVertex(); node++) {
                assertEquals(set.contains(node), mySet.contains(node));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("small1")
    void calcMod_shouldGiveCorrectMod(Graph g, Set<Integer> edgesToKill,
                                      Map<Integer, Set<Integer>> optCommSets, double mod) {
        Solver sv = new Solver();
        Solution s = new Solution(g);
        Problem p = new Problem(s.getNumEdge(), 1);

        sv.getEdgesToKill(s, p);
        sv.killEdges(s, p);
        sv.calcMod(s, p);

        assertEquals(mod, p.getMod(), EPSILON);
    }

    @Test
    void solve_shouldGiveCorrectResult() {
        fail("Not implemented yet.");
    }
}