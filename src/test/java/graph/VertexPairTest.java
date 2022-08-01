package graph;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class VertexPairTest {
    static Stream<Arguments> range() {
        return Stream.of(
                arguments(1, 2),
                arguments(3, 5),
                arguments(100, 1000),
                arguments(5325, 612),
                arguments(1, 1),
                arguments(63289, 63289),
                arguments(613563, 136230)
        );
    }

    @ParameterizedTest
    @MethodSource("range")
    void sameVerticesShouldEqual(int val1, int val2) {
        VertexPair vp1 = new VertexPair(val1, val2);
        VertexPair vp2 = new VertexPair(val1, val2);

        assertEquals(vp1, vp2);
    }

    @ParameterizedTest
    @MethodSource("range")
    void swappedVerticesShouldEqual(int val1, int val2) {
        VertexPair vp1 = new VertexPair(val1, val2);
        VertexPair vp2 = new VertexPair(val2, val1);

        assertEquals(vp1, vp2);
    }

    @ParameterizedTest
    @MethodSource("range")
    void sameAndSwappedVerticesCanBeFoundInHashSet(int val1, int val2) {
        HashSet<VertexPair> vp = new HashSet<>();
        vp.add(new VertexPair(val1, val2));
        assertTrue(vp.contains(new VertexPair(val1, val2)));
        assertTrue(vp.contains(new VertexPair(val2, val1)));
    }

    static Stream<Arguments> twoRanges() {
        return Stream.of(
                arguments(3, 7, 2, 9),
                arguments(1, 2, 1, 3),
                arguments(1, 2, 5, 1),
                arguments(1, 2, 2, 10),
                arguments(1, 2, 10, 2),
                arguments(629, 2398, 6238, 2347),
                arguments(627358, 235823, 125783, 235793),
                arguments(394820, 673920, 103683, 394820)
        );
    }

    @ParameterizedTest
    @MethodSource("twoRanges")
    void atLeastOneDifferentVertexShouldNotEqual(int val1, int val2, int val3, int val4) {
        VertexPair vp1 = new VertexPair(val1, val2);
        VertexPair vp2 = new VertexPair(val3, val4);

        assertNotEquals(vp1, vp2);
    }

    @ParameterizedTest
    @MethodSource("twoRanges")
    void otherVerticesCannotBeFoundInHashSet(int val1, int val2, int val3, int val4) {
        HashSet<VertexPair> vp = new HashSet<>();
        vp.add(new VertexPair(val1, val2));
        assertFalse(vp.contains(new VertexPair(val3, val4)));
        assertFalse(vp.contains(new VertexPair(val4, val3)));
    }
}