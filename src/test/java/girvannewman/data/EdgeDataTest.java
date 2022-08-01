package girvannewman.data;

import graph.VertexPair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class EdgeDataTest {
    static Stream<Arguments> values() {
        return Stream.of(
                arguments(0, 1, 2),
                arguments(142, 328, 641),
                arguments(532839, 325124, 648940)
        );
    }

    @ParameterizedTest
    @MethodSource("values")
    void ctor_shouldHaveCorrectValues(int node1, int node2, int edge) {
        EdgeData e = new EdgeData(node1, node2, edge);

        assertEquals(edge, e.getEdgeIndex());
        assertEquals(new VertexPair(node1, node2), e.getEndpoints());
        assertTrue(e.isAlive());
        assertEquals(-1, e.getIterKilled());
    }

    @Test
    void ctor_nonPositiveIndexShouldThrowIAE() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    EdgeData e = new EdgeData(-1, 1, 1);
                });

        assertThrows(IllegalArgumentException.class,
                () -> {
                    EdgeData e = new EdgeData(1, -1, 2);
                });

        assertThrows(IllegalArgumentException.class,
                () -> {
                    EdgeData e = new EdgeData(3, 4, -1);
                });
    }

    @Test
    void ctor_equalNodeIndexShouldThrowIAE() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    EdgeData e = new EdgeData(1, 1, 3);
                });

        assertThrows(IllegalArgumentException.class,
                () -> {
                    EdgeData e = new EdgeData(64395, 64395, 6328);
                });
    }

    static Stream<Arguments> killValues() {
        return Stream.of(
                arguments(0, 1, 2, 25),
                arguments(142, 328, 641, 64),
                arguments(532839, 325124, 648940, 61032)
        );
    }

    @ParameterizedTest
    @MethodSource("killValues")
    void killSetsAliveFalseAndSetIterKilled(int node1, int node2, int edge, int killIter) {
        EdgeData e = new EdgeData(node1, node2, edge);
        e.kill(killIter);
        assertFalse(e.isAlive());
        assertEquals(killIter, e.getIterKilled());
    }

    @Test
    void killThrowsIAEWithNonpositiveIter() {
        EdgeData e = new EdgeData(1, 2, 3);
        assertThrows(IllegalArgumentException.class,
                () -> {
                    e.kill(0);
                });

        assertThrows(IllegalArgumentException.class,
                () -> {
                    e.kill(-1);
                });
    }
}