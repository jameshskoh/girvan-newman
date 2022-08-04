package girvannewman;

import girvannewman.data.FlowData;
import graph.Graph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static util.Constants.EPSILON;

class ProblemTest {
    static Stream<Arguments> values() {
        return Stream.of(
                Arguments.arguments(0, 1),
                Arguments.arguments(1, 1),
                Arguments.arguments(2, 5),
                Arguments.arguments(5, 2),
                Arguments.arguments(365091, 1209750)
        );
    }

    @ParameterizedTest
    @MethodSource("values")
    void ctor_allValuesAreCorrect(int numEdge, int iter) {
        Problem p = new Problem(numEdge, iter);

        assertEquals(iter, p.getIter());

        for (int edge = 0; edge < numEdge; edge++) {
            assertEquals(0.0, p.getBetw(edge), EPSILON);
        }

        assertEquals(0, p.getCommunitiesSet().size());
        assertEquals(0.0, p.getMod(), EPSILON);
    }

    @Test
    void ctor_negativeEdgeNumberShouldThrowIAE() {
        assertThrows(IllegalArgumentException.class,
                () -> new Problem(-1, 1));
    }

    @Test
    void ctor_nonPositiveIterShouldThrowIAE() {
        assertThrows(IllegalArgumentException.class,
                () -> new Problem(1, -1));

        assertThrows(IllegalArgumentException.class,
                () -> new Problem(1, 0));
    }

    static Stream<Arguments> smallSet() {
        return Stream.of(
                Arguments.arguments(5, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("smallSet")
    void incBetw_shouldIncBetw(int numEdge, int iter) {
        Problem p = new Problem(numEdge, iter);

        p.incBetw(0, 10.0);
        assertEquals(10.0, p.getBetw(0), EPSILON);

        p.incBetw(0, 20.0);
        assertEquals(30.0, p.getBetw(0), EPSILON);

        p.incBetw(1, 20.0);
        assertEquals(20.0, p.getBetw(1), EPSILON);

        p.incBetw(1, 40.0);
        assertEquals(60.0, p.getBetw(1), EPSILON);

        p.incBetw(2, 349.13789345);
        assertEquals(349.13789345, p.getBetw(2), EPSILON);

        p.incBetw(3, 2.0/5);
        assertEquals(0.4, p.getBetw(3), EPSILON);

        p.incBetw(3, 3.0/5);
        assertEquals(1.0, p.getBetw(3), EPSILON);

        p.incBetw(4, 643705039);
        assertEquals(643705039, p.getBetw(4), EPSILON);
    }

    @ParameterizedTest
    @MethodSource("smallSet")
    void incBetw_invalidEdgeShouldThrowIAE(int numEdge, int iter) {
        Problem p = new Problem(numEdge, iter);

        assertThrows(IllegalArgumentException.class,
                () -> p.incBetw(-1, 100.0));

        assertThrows(IllegalArgumentException.class,
                () -> p.incBetw(-100, 100.0));

        assertThrows(IllegalArgumentException.class,
                () -> p.incBetw(numEdge, 100.0));

        assertThrows(IllegalArgumentException.class,
                () -> p.incBetw(numEdge + 1, 100.0));
    }

    @ParameterizedTest
    @MethodSource("smallSet")
    void incBetw_negativeIncShouldThrowIAE(int numEdge, int iter) {
        Problem p = new Problem(numEdge, iter);

        assertThrows(IllegalArgumentException.class,
                () -> p.incBetw(0, -1.0));

        assertThrows(IllegalArgumentException.class,
                () -> p.incBetw(0, -100.0));

        assertThrows(IllegalArgumentException.class,
                () -> p.incBetw(0, -0.00000001));
    }

    @ParameterizedTest
    @MethodSource("smallSet")
    void getEdgesToKill_shouldReturnCorrectSet(int numEdge, int iter) {
        Problem p = new Problem(numEdge, iter);

        Set<Integer> edgesToKill = Stream.of(1, 3, 4)
                .collect(Collectors.toSet());

        p.setEdgesToKill(edgesToKill);

        Set<Integer> myEdgesToKill = p.getEdgesToKill();

        for (int edge = 0; edge < numEdge; edge++) {
            if (edgesToKill.contains(edge)) {
                assertTrue(myEdgesToKill.contains(edge));
            } else {
                assertFalse(myEdgesToKill.contains(edge));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("smallSet")
    void getFlowData_shouldReturnCorrectFlowData(int numEdge, int iter) {
        Problem p = new Problem(numEdge, iter);

        Map<Integer, FlowData> flowsData = p.getFlowsData();

        assertTrue(flowsData.isEmpty());

        flowsData.put(0, new FlowData(0));
        flowsData.put(1, new FlowData(1));
        flowsData.put(3, new FlowData(3));
        flowsData.put(5, new FlowData(5));

        assertEquals(0, p.getFlowData(0).getNode());
        assertEquals(1, p.getFlowData(1).getNode());
        assertEquals(3, p.getFlowData(3).getNode());
        assertEquals(5, p.getFlowData(5).getNode());
    }

    @ParameterizedTest
    @MethodSource("smallSet")
    void getFlowData_nonexistentIndexShouldThrowNSEE(int numEdge, int iter) {
        Problem p = new Problem(numEdge, iter);

        Map<Integer, FlowData> flowsData = p.getFlowsData();

        assertTrue(flowsData.isEmpty());

        flowsData.put(0, new FlowData(0));
        flowsData.put(1, new FlowData(1));
        flowsData.put(3, new FlowData(3));
        flowsData.put(5, new FlowData(5));

        assertThrows(NoSuchElementException.class,
                () -> p.getFlowData(-1));

        assertThrows(NoSuchElementException.class,
                () -> p.getFlowData(2));

        assertThrows(NoSuchElementException.class,
                () -> p.getFlowData(4));

        assertThrows(NoSuchElementException.class,
                () -> p.getFlowData(6));

        assertThrows(NoSuchElementException.class,
                () -> p.getFlowData(100));
    }

    @ParameterizedTest
    @MethodSource("smallSet")
    void resetFlowsData_shouldResetFlowsData(int numEdge, int iter) {
        Problem p = new Problem(numEdge, iter);

        Map<Integer, FlowData> flowsData = p.getFlowsData();

        assertTrue(flowsData.isEmpty());

        flowsData.put(0, new FlowData(0));
        flowsData.put(1, new FlowData(1));
        flowsData.put(3, new FlowData(3));
        flowsData.put(5, new FlowData(5));

        p.resetFlowsData();

        assertEquals(0, p.getFlowsData().size());
    }

    @ParameterizedTest
    @MethodSource("smallSet")
    void initNewComm_shouldCreateNewCommunityAndReturnIndex(int numEdge, int iter) {
        Problem p = new Problem(numEdge, iter);
        Map<Integer, Set<Integer>> m;
        int commIndex;

        commIndex = p.initNewComm();
        assertEquals(0, commIndex);

        m = p.getCommunitiesSet();
        assertEquals(1, m.size());
        assertTrue(m.containsKey(0));

        commIndex = p.initNewComm();
        assertEquals(1, commIndex);

        m = p.getCommunitiesSet();
        assertEquals(2, m.size());
        assertTrue(m.containsKey(1));

        commIndex = p.initNewComm();
        assertEquals(2, commIndex);

        m = p.getCommunitiesSet();
        assertEquals(3, m.size());
        assertTrue(m.containsKey(2));
    }

    @ParameterizedTest
    @MethodSource("smallSet")
    void putInComm_shouldPutNodeInCorrectComm(int numEdge, int iter) {
        Problem p = new Problem(numEdge, iter);
        int commIndex;

        commIndex = p.initNewComm();
        p.putInComm(commIndex, 0);
        p.putInComm(commIndex, 1);
        p.putInComm(commIndex, 2);

        commIndex = p.initNewComm();
        p.putInComm(commIndex, 3);
        p.putInComm(commIndex, 4);

        commIndex = p.initNewComm();
        p.putInComm(commIndex, 5);
        p.putInComm(commIndex, 6);

        Map<Integer, Set<Integer>> m = p.getCommunitiesSet();

        assertTrue(m.get(0).contains(0));
        assertTrue(m.get(0).contains(1));
        assertTrue(m.get(0).contains(2));
        assertTrue(m.get(1).contains(3));
        assertTrue(m.get(1).contains(4));
        assertTrue(m.get(2).contains(5));
        assertTrue(m.get(2).contains(6));

        assertFalse(m.get(1).contains(1));
        assertFalse(m.get(2).contains(1));

        assertFalse(m.get(0).contains(3));
        assertFalse(m.get(2).contains(3));

        assertFalse(m.get(0).contains(6));
        assertFalse(m.get(1).contains(6));
    }

    @ParameterizedTest
    @MethodSource("smallSet")
    void isInComm_shouldTellCorrectly(int numEdge, int iter) {
        Problem p = new Problem(numEdge, iter);
        int commIndex;

        commIndex = p.initNewComm();
        p.putInComm(commIndex, 0);
        p.putInComm(commIndex, 1);
        p.putInComm(commIndex, 2);

        commIndex = p.initNewComm();
        p.putInComm(commIndex, 3);
        p.putInComm(commIndex, 4);

        commIndex = p.initNewComm();
        p.putInComm(commIndex, 5);
        p.putInComm(commIndex, 6);

        assertFalse(p.isInComm(-1));
        assertTrue(p.isInComm(0));
        assertTrue(p.isInComm(1));
        assertTrue(p.isInComm(2));
        assertTrue(p.isInComm(3));
        assertTrue(p.isInComm(4));
        assertTrue(p.isInComm(5));
        assertTrue(p.isInComm(6));
        assertFalse(p.isInComm(7));
        assertFalse(p.isInComm(8));
        assertFalse(p.isInComm(273985));
    }

    @ParameterizedTest
    @MethodSource("smallSet")
    void incMod_shouldIncMod(int numEdge, int iter) {
        Problem p = new Problem(numEdge, iter);

        p.incMod(0.5);
        assertEquals(0.5, p.getMod(), EPSILON);

        p.incMod(250.0);
        assertEquals(250.5, p.getMod(), EPSILON);

        p.incMod(624798523);
        assertEquals(624798773.5, p.getMod(), EPSILON);
    }
}