package girvannewman.data;

import graph.VertexPair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static util.Constants.EPSILON;

class FlowDataTest {
    static Stream<Arguments> values() {
        return Stream.of(
                Arguments.arguments(0, 1),
                Arguments.arguments(1, 2),
                Arguments.arguments(2, 3),
                Arguments.arguments(100, 4),
                Arguments.arguments(672850, 34698)
        );
    }

    @ParameterizedTest
    @MethodSource("values")
    void ctor_hasCorrectValues(int value, int level) {
        FlowData fd = new FlowData(value, level);

        assertEquals(value, fd.getNode());
        assertEquals(level, fd.getLevel());
        assertEquals(0, fd.getUpstream().size());
        assertEquals(0, fd.getDownstream().size());
        assertEquals(0, fd.getPathCount());
        assertEquals(0.0, fd.getFlowCount(), EPSILON);
    }

    @Test
    void ctor_negativeValueShouldThrowIAE() {
        assertThrows(IllegalArgumentException.class,
                () -> new FlowData(-1, 1));
    }

    @Test
    void ctor_negativeLevelShouldThrowIAE() {
        assertThrows(IllegalArgumentException.class,
                () -> new FlowData(1, -1));
    }

    static Stream<Arguments> data() {
        List<Integer> parentPaths = Stream.of(2, 3)
                .collect(Collectors.toList());

        List<Integer> nodes = Stream.of(0, 1, 2, 3, 4)
                .collect(Collectors.toList());

        List<Integer> levels = Stream.of(1, 1, 2, 3, 3)
                .collect(Collectors.toList());

        List<Set<Integer>> neighborsSet = Stream.of(
                Stream.of(2).collect(Collectors.toSet()),
                Stream.of(2).collect(Collectors.toSet()),
                Stream.of(0, 1, 3, 4).collect(Collectors.toSet()),
                Stream.of(2).collect(Collectors.toSet()),
                Stream.of(2).collect(Collectors.toSet())
        ).collect(Collectors.toList());

        List<Set<Integer>> upstreamSet = Stream.of(
                new HashSet<Integer>(),
                new HashSet<Integer>(),
                Stream.of(0, 1).collect(Collectors.toSet()),
                Stream.of(2).collect(Collectors.toSet()),
                Stream.of(2).collect(Collectors.toSet())
        ).collect(Collectors.toList());

        List<Set<Integer>> downstreamSet = Stream.of(
                Stream.of(2).collect(Collectors.toSet()),
                Stream.of(2).collect(Collectors.toSet()),
                Stream.of(3, 4).collect(Collectors.toSet()),
                new HashSet<Integer>(),
                new HashSet<Integer>()
        ).collect(Collectors.toList());

        List<Integer> pathCounts = Stream.of(2, 3, 5, 5, 5)
                .collect(Collectors.toList());

        List<Double> flowCounts = Stream.of(
                3.0 * 2 / 5, 3.0 * 3 / 5, 3.0, 1.0, 1.0
        ).collect(Collectors.toList());

        Map<VertexPair, Double> betwInc = Map.of(
                new VertexPair(0, 2), 3.0 * 2 / 5,
                new VertexPair(1, 2), 3.0 * 3 / 5,
                new VertexPair(2, 3), 1.0,
                new VertexPair(2, 4), 1.0
        );

        return Stream.of(
                Arguments.arguments(
                        parentPaths, nodes, levels, neighborsSet,
                        upstreamSet, downstreamSet, pathCounts,
                        flowCounts, betwInc)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    void populateDownstream_shouldAddDownstreamToFlowData(
            List<Integer> parentPaths, List<Integer> nodes, List<Integer> levels,
            List<Set<Integer>> neighborsSet,
            List<Set<Integer>> upstreamSet, List<Set<Integer>> downstreamSet,
            List<Integer> pathCounts, List<Double> flowCounts, Map<VertexPair, Double> betwInc) {
        Map<Integer, FlowData> flowsData = new HashMap<>();

        // set up parents
        for (int node = 0; node < parentPaths.size(); node++) {
            FlowData parent = new FlowData(node, 1);
            parent.incPathCount(parentPaths.get(node));
            flowsData.put(node, parent);
        }

        // check all nodes from parents to children
        for (int node : nodes) {
            FlowData currNode = flowsData.get(node);
            currNode.populateDownstream(flowsData, neighborsSet.get(node), new HashSet<>());
            currNode.updateDownstream(flowsData);

            assertEquals(downstreamSet.get(node).size(), currNode.getDownstream().size());

            for (int neighbor : nodes) {
                if (downstreamSet.get(node).contains(neighbor)) {
                    assertTrue(currNode.getDownstream().contains(neighbor));
                } else {
                    assertFalse(currNode.getDownstream().contains(neighbor));
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    void populateDownstream_shouldAddAllNodesToFlowsData(
            List<Integer> parentPaths, List<Integer> nodes, List<Integer> levels,
            List<Set<Integer>> neighborsSet,
            List<Set<Integer>> upstreamSet, List<Set<Integer>> downstreamSet,
            List<Integer> pathCounts, List<Double> flowCounts, Map<VertexPair, Double> betwInc) {
        Map<Integer, FlowData> flowsData = new HashMap<>();

        // set up parents
        for (int node = 0; node < parentPaths.size(); node++) {
            FlowData parent = new FlowData(node, 1);
            parent.incPathCount(parentPaths.get(node));
            flowsData.put(node, parent);
        }

        for (int node : nodes) {
            FlowData currNode = flowsData.get(node);
            currNode.populateDownstream(flowsData, neighborsSet.get(node), new HashSet<>());
            currNode.updateDownstream(flowsData);
        }

        // check flowsData
        assertEquals(nodes.size(), flowsData.size());

        for (int node : nodes) {
            assertTrue(flowsData.containsKey(node));
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    void populateDownstream_shouldSetCorrectLevel(
            List<Integer> parentPaths, List<Integer> nodes, List<Integer> levels,
            List<Set<Integer>> neighborsSet,
            List<Set<Integer>> upstreamSet, List<Set<Integer>> downstreamSet,
            List<Integer> pathCounts, List<Double> flowCounts, Map<VertexPair, Double> betwInc) {
        Map<Integer, FlowData> flowsData = new HashMap<>();

        // set up parents
        for (int node = 0; node < parentPaths.size(); node++) {
            FlowData parent = new FlowData(node, 1);
            parent.incPathCount(parentPaths.get(node));
            flowsData.put(node, parent);
        }

        for (int node : nodes) {
            FlowData currNode = flowsData.get(node);
            currNode.populateDownstream(flowsData, neighborsSet.get(node), new HashSet<>());
            currNode.updateDownstream(flowsData);
        }

        for (int node : nodes) {
            assertEquals(levels.get(node), flowsData.get(node).getLevel());
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    void updateDownstream_shouldIncDownstreamPathCount(
            List<Integer> parentPaths, List<Integer> nodes, List<Integer> levels,
            List<Set<Integer>> neighborsSet,
            List<Set<Integer>> upstreamSet, List<Set<Integer>> downstreamSet,
            List<Integer> pathCounts, List<Double> flowCounts, Map<VertexPair, Double> betwInc) {
        Map<Integer, FlowData> flowsData = new HashMap<>();

        // set up parents
        for (int node = 0; node < parentPaths.size(); node++) {
            FlowData parent = new FlowData(node, 1);
            parent.incPathCount(parentPaths.get(node));
            flowsData.put(node, parent);
        }

        // check all nodes from parents to children
        for (int node : nodes) {
            FlowData currNode = flowsData.get(node);
            currNode.populateDownstream(flowsData, neighborsSet.get(node), new HashSet<>());
            currNode.updateDownstream(flowsData);

            assertEquals(pathCounts.get(node), currNode.getPathCount());
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    void updateDownstream_downstreamShouldAddSelfAsUpstream(
            List<Integer> parentPaths, List<Integer> nodes, List<Integer> levels,
            List<Set<Integer>> neighborsSet,
            List<Set<Integer>> upstreamSet, List<Set<Integer>> downstreamSet,
            List<Integer> pathCounts, List<Double> flowCounts, Map<VertexPair, Double> betwInc) {
        Map<Integer, FlowData> flowsData = new HashMap<>();

        // set up parents
        for (int node = 0; node < parentPaths.size(); node++) {
            FlowData parent = new FlowData(node, 1);
            parent.incPathCount(parentPaths.get(node));
            flowsData.put(node, parent);
        }

        // check all nodes from parents to children
        for (int node : nodes) {
            FlowData currNode = flowsData.get(node);
            currNode.populateDownstream(flowsData, neighborsSet.get(node), new HashSet<>());
            currNode.updateDownstream(flowsData);

            assertEquals(upstreamSet.get(node).size(), currNode.getUpstream().size());

            for (int neighbor : nodes) {
                if (upstreamSet.get(node).contains(neighbor)) {
                    assertTrue(currNode.getUpstream().contains(neighbor));
                } else {
                    assertFalse(currNode.getUpstream().contains(neighbor));
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    void incUpstreamFlow_shouldUpdateUpstreamFlowAndReturnCorrectBetwInc(
            List<Integer> parentPaths, List<Integer> nodes, List<Integer> levels,
            List<Set<Integer>> neighborsSet,
            List<Set<Integer>> upstreamSet, List<Set<Integer>> downstreamSet,
            List<Integer> pathCounts, List<Double> flowCounts, Map<VertexPair, Double> betwInc) {
        Map<Integer, FlowData> flowsData = new HashMap<>();

        // set up parents
        for (int node = 0; node < parentPaths.size(); node++) {
            FlowData parent = new FlowData(node, 1);
            parent.incPathCount(parentPaths.get(node));
            flowsData.put(node, parent);
        }

        // check all nodes from parents to children
        for (int node : nodes) {
            FlowData currNode = flowsData.get(node);
            currNode.populateDownstream(flowsData, neighborsSet.get(node), new HashSet<>());
            currNode.updateDownstream(flowsData);
        }

        for (int node = nodes.size() - 1; node >= 0; node--) {
            FlowData currNode = flowsData.get(node);
            Map<VertexPair, Double> currBetw = currNode.incUpstreamFlow(flowsData);

            assertEquals(flowCounts.get(node), currNode.getFlowCount(), EPSILON);

            assertEquals(upstreamSet.get(node).size(), currBetw.size());

            for (int u : upstreamSet.get(node)) {
                VertexPair vp = new VertexPair(node, u);
                assertEquals(betwInc.get(vp), currBetw.get(vp), EPSILON);
            }
        }
    }
}