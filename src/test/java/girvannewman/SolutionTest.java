package girvannewman;

import girvannewman.data.EdgeData;
import graph.Graph;
import graph.VertexPair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.GraphLoader;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static util.Constants.EPSILON;

class SolutionTest {
    static Stream<Arguments> small1() {
        Graph g = new Graph();
        try {
            GraphLoader.loadGraph(g, "data/small1.edge");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("Should not throw FNFE.");
        }

        return Stream.of(
                Arguments.arguments(g)
        );
    }

    @ParameterizedTest
    @MethodSource("small1")
    void ctor_allNumShouldBeCorrect(Graph g) {
        Solution s = new Solution(g);
        assertEquals(g.getNumVertex(), s.getNumVert());
        assertEquals(g.getNumEdge(), s.getNumEdge());

        assertEquals(1, s.getObjList().size());
        assertEquals(0.0, s.getObjList().get(0), EPSILON);

        assertEquals(1, s.getKilledEdgeList().size());
        assertTrue(s.getKilledEdgeList().get(0).isEmpty());
    }

    @Test
    void ctor_nullGraphShouldThrowNPE() {
        assertThrows(NullPointerException.class,
                () -> new Solution(null));
    }

    @ParameterizedTest
    @MethodSource("small1")
    void getNeighborsOf_allNeighborsShouldBeCorrect(Graph g) {
        Solution s = new Solution(g);
        Map<Integer, Set<Integer>> n = g.exportGraph();

        for (int node1 : n.keySet()) {
            Set<Integer> myNeighbors = s.getNeighborsOf(node1);

            for (int node2 : n.keySet()) {
                if (n.get(node1).contains(node2)) {
                    assertTrue(myNeighbors.contains(node2));
                } else {
                    assertFalse(myNeighbors.contains(node2));
                }
            }

            assertFalse(myNeighbors.contains(-1));
            assertFalse(myNeighbors.contains(-100));
            assertFalse(myNeighbors.contains(g.getNumVertex()));
            assertFalse(myNeighbors.contains(g.getNumVertex() + 1));
            assertFalse(myNeighbors.contains(g.getNumVertex() + 2));
            assertFalse(myNeighbors.contains(g.getNumVertex() + 100));
        }
    }

    @ParameterizedTest
    @MethodSource("small1")
    void getNeighborsOf_invalidNodeShouldThrowIAE(Graph g) {
        Solution s = new Solution(g);
        Map<Integer, Set<Integer>> n = g.exportGraph();

        assertThrows(IllegalArgumentException.class,
                () -> s.getNeighborsOf(s.getNumVert()));

        assertThrows(IllegalArgumentException.class,
                () -> s.getNeighborsOf(s.getNumVert() + 1));

        assertThrows(IllegalArgumentException.class,
                () -> s.getNeighborsOf(s.getNumVert() + 100));

        assertThrows(IllegalArgumentException.class,
                () -> s.getNeighborsOf(-1));
    }

    @ParameterizedTest
    @MethodSource("small1")
    void getNumNeighborsOf_numNeighborsShouldBeCorrect(Graph g) {
        Solution s = new Solution(g);
        Map<Integer, Set<Integer>> n = g.exportGraph();

        for (int node1 : n.keySet()) {
            assertEquals(n.get(node1).size(), s.getNumNeighborsOf(node1));
        }
    }

    @ParameterizedTest
    @MethodSource("small1")
    void areNeighbors_shouldBeCorrect(Graph g) {
        Solution s = new Solution(g);
        Map<Integer, Set<Integer>> n = g.exportGraph();

        for (int node1 : n.keySet()) {
            for (int node2 : n.keySet()) {
                if (n.get(node1).contains(node2)) {
                    assertTrue(s.areNeighbors(node1, node2));
                } else {
                    assertFalse(s.areNeighbors(node1, node2));
                }
            }

            assertFalse(s.areNeighbors(node1, -1));
            assertFalse(s.areNeighbors(node1, -100));
            assertFalse(s.areNeighbors(node1, g.getNumVertex()));
            assertFalse(s.areNeighbors(node1, g.getNumVertex() + 1));
            assertFalse(s.areNeighbors(node1, g.getNumVertex() + 2));
            assertFalse(s.areNeighbors(node1, g.getNumVertex() + 100));
        }
    }

    @ParameterizedTest
    @MethodSource("small1")
    void areNeighbors_invalidNodeShouldReturnFalse(Graph g) {
        Solution s = new Solution(g);
        Map<Integer, Set<Integer>> n = g.exportGraph();

        for (int node = 0; node < g.getNumVertex(); node++) {
            assertFalse(s.areNeighbors(node, g.getNumVertex()));
            assertFalse(s.areNeighbors(g.getNumVertex(), node));
            assertFalse(s.areNeighbors(node, -1));
            assertFalse(s.areNeighbors(-1, node));
        }

        assertFalse(s.areNeighbors(-1, -1));
        assertFalse(s.areNeighbors(g.getNumVertex(), g.getNumVertex()));
    }

    @ParameterizedTest
    @MethodSource("small1")
    void hasEdge_shouldBeCorrect(Graph g) {
        Solution s = new Solution(g);
        Map<Integer, Set<Integer>> n = g.exportGraph();

        for (int node1 = 0; node1 < n.size(); node1++) {
            for (int node2 = 0; node2 < n.size(); node2++) {
                if (n.get(node1).contains(node2)) {
                    assertTrue(s.hasEdge(node1, node2));
                    assertTrue(s.hasEdge(node2, node1));
                } else {
                    assertFalse(s.hasEdge(node1, node2));
                    assertFalse(s.hasEdge(node2, node1));
                }
            }
        }

        assertFalse(s.hasEdge(-1, 100));
        assertFalse(s.hasEdge(100, -1));
        assertFalse(s.hasEdge(-1, -1));
    }

    @ParameterizedTest
    @MethodSource("small1")
    void getEdgeData_verticesShouldReturnCorrectEdge(Graph g) {
        Solution s = new Solution(g);
        Map<Integer, Set<Integer>> n = g.exportGraph();

        for (int node = 0; node < n.size(); node++) {
            for (int neighbor : n.get(node)) {
                EdgeData ed = s.getEdgeData(node, neighbor);
                assertEquals(new VertexPair(node, neighbor), ed.getEndpoints());
            }
        }
    }

    @ParameterizedTest
    @MethodSource("small1")
    void getEdgeData_edgeShouldReturnCorrectEdge(Graph g) {
        Solution s = new Solution(g);
        int numEdge = g.getNumEdge();

        for (int edge = 0; edge < numEdge; edge++) {
            EdgeData ed = s.getEdgeData(edge);
            assertEquals(edge, ed.getEdgeIndex());
        }
    }

    static Stream<Arguments> obj() {
        Graph g = new Graph();
        try {
            GraphLoader.loadGraph(g, "data/small1.edge");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("Should not throw FNFE.");
        }

        List<Set<Integer>> list1 = Stream.of(
                    Stream.of(0, 1).collect(Collectors.toSet()),
                    Stream.of(2, 3).collect(Collectors.toSet()),
                    Stream.of(4).collect(Collectors.toSet()),
                    Stream.of(5).collect(Collectors.toSet())
        ).collect(Collectors.toList());

        List<Set<Integer>> list2 = Stream.of(
                Stream.of(100, 200).collect(Collectors.toSet()),
                Stream.of(356, 464).collect(Collectors.toSet()),
                Stream.of(5236).collect(Collectors.toSet())
        ).collect(Collectors.toList());

        List<Set<Integer>> list3 = Stream.of(
                Stream.of(640523, 382353).collect(Collectors.toSet()),
                Stream.of(112567, 709647).collect(Collectors.toSet()),
                Stream.of(753209).collect(Collectors.toSet()),
                Stream.of(999999).collect(Collectors.toSet()),
                Stream.of(6832093).collect(Collectors.toSet())
        ).collect(Collectors.toList());

        return Stream.of(
                Arguments.arguments(
                        new double[]{1.0, 2.0, 3.0, 4.0},
                        list1,
                        4),
                Arguments.arguments(
                        new double[]{2.5, 3.5, 2.2},
                        list2,
                        2),
                Arguments.arguments(
                        new double[]{2.5, 3.5, 2.2, 4.6, 3.0},
                        list3,
                        4)
        );
    }

    @ParameterizedTest
    @MethodSource("obj")
    void addResult_shouldAddCorrectObjs(
            double[] objList, List<Set<Integer>> killedLists, int optIter) {
        Solution s = new Solution(new Graph());

        for (int iter = 0; iter < objList.length; iter++) {
            s.addResultAndSetOpt(objList[iter], killedLists.get(iter));
        }

        List<Double> myObjList = s.getObjList();

        for (int iter = 1; iter <= objList.length; iter++) {
            assertEquals(objList[iter-1], myObjList.get(iter), EPSILON);
        }
    }

    @ParameterizedTest
    @MethodSource("obj")
    void addResult_shouldAddCorrectKilledEdges(
            double[] objList, List<Set<Integer>> killedLists, int optIter) {
        Solution s = new Solution(new Graph());

        for (int iter = 0; iter < objList.length; iter++) {
            s.addResultAndSetOpt(objList[iter], killedLists.get(iter));
        }

        List<Set<Integer>> myKilledLists = s.getKilledEdgeList();

        for (int iter = 0; iter < killedLists.size(); iter++) {
            Set<Integer> myList = myKilledLists.get(iter + 1);

            for (int index : killedLists.get(iter)) {
                assertTrue(myList.contains(index));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("obj")
    void addResult_shouldSetCorrectOptIter(
            double[] objList, List<Set<Integer>> killedLists, int optIter) {
        Solution s = new Solution(new Graph());

        for (int iter = 0; iter < objList.length; iter++) {
            s.addResultAndSetOpt(objList[iter], killedLists.get(iter));
        }

        int myOptIter = s.getOptIter();

        assertEquals(optIter, myOptIter);
    }

    @Test
    void addResult_nullKilledEdgesShouldThrowNPE() {
        Solution s = new Solution(new Graph());

        assertThrows(NullPointerException.class,
                () -> s.addResultAndSetOpt(1.0, null));
    }

    @Test
    void setOptCommunitiesSet_nullOptCommSetShouldThrowNPE() {
        Solution s = new Solution(new Graph());

        assertThrows(NullPointerException.class,
                () -> s.setOptCommSets(null));
    }

    @Test
    void getOptCommunitiesSet_shouldGiveCorrectCommunity() {
        Solution s = new Solution(new Graph());

        Map<Integer, Set<Integer>> optCommSet = new HashMap<>();
        optCommSet.put(
                0,
                Stream.of(0, 1, 2).collect(Collectors.toSet())
        );
        optCommSet.put(
                1,
                Stream.of(3, 4, 5).collect(Collectors.toSet())
        );


        s.setOptCommSets(optCommSet);

        Map<Integer, Set<Integer>> myOptCommSet = s.getOptCommSets();

        assertEquals(2, myOptCommSet.size());

        Set<Integer> set1 = myOptCommSet.get(0);
        assertTrue(set1.contains(0));
        assertTrue(set1.contains(1));
        assertTrue(set1.contains(2));
        assertFalse(set1.contains(3));
        assertFalse(set1.contains(4));
        assertFalse(set1.contains(5));
        assertFalse(set1.contains(6));
        assertFalse(set1.contains(100));
        assertFalse(set1.contains(-1));


        Set<Integer> set2 = myOptCommSet.get(1);
        assertTrue(set2.contains(3));
        assertTrue(set2.contains(4));
        assertTrue(set2.contains(5));
        assertFalse(set2.contains(0));
        assertFalse(set2.contains(1));
        assertFalse(set2.contains(2));
        assertFalse(set2.contains(6));
        assertFalse(set2.contains(100));
        assertFalse(set2.contains(-1));
    }
}