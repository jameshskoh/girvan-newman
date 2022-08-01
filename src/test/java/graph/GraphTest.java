package graph;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    @Test
    void ctor_ShouldBeEmptyAndCountsZero() {
        Graph g = new Graph();
        assertEquals(0, g.getNumVertex());
        assertEquals(0, g.getNumEdge());

        Map<Integer, Set<Integer>> n = g.exportGraph();
        assertEquals(0, n.size());
    }

    @Test
    void addVertexShouldIncreaseVertexCount() {
        Graph g = new Graph();
        Map<Integer, Set<Integer>> n;

        g.addVertex(2);
        assertEquals(1, g.getNumVertex());
        assertEquals(0, g.getNumEdge());
        n = g.exportGraph();
        assertEquals(1, n.size());

        g.addVertex(0);
        assertEquals(2, g.getNumVertex());
        assertEquals(0, g.getNumEdge());
        n = g.exportGraph();
        assertEquals(2, n.size());

        g.addVertex(1);
        assertEquals(3, g.getNumVertex());
        assertEquals(0, g.getNumEdge());
        n = g.exportGraph();
        assertEquals(3, n.size());
    }

    @Test
    void addVertexRepeatedlyShouldOnlyCountOnce() {
        Graph g = new Graph();
        Map<Integer, Set<Integer>> n;

        g.addVertex(1);
        assertEquals(1, g.getNumVertex());
        assertEquals(0, g.getNumEdge());
        n = g.exportGraph();
        assertEquals(1, n.size());

        g.addVertex(2);
        assertEquals(2, g.getNumVertex());
        assertEquals(0, g.getNumEdge());
        n = g.exportGraph();
        assertEquals(2, n.size());

        g.addVertex(2);
        assertEquals(2, g.getNumVertex());
        assertEquals(0, g.getNumEdge());
        n = g.exportGraph();
        assertEquals(2, n.size());

        g.addVertex(3);
        assertEquals(3, g.getNumVertex());
        assertEquals(0, g.getNumEdge());
        n = g.exportGraph();
        assertEquals(3, n.size());

        g.addVertex(2);
        assertEquals(3, g.getNumVertex());
        assertEquals(0, g.getNumEdge());
        n = g.exportGraph();
        assertEquals(3, n.size());
    }

    @Test
    void addedVertexShouldExistAndHaveNoNeighbor() {
        Graph g = new Graph();
        Map<Integer, Set<Integer>> n;

        g.addVertex(2);
        n = g.exportGraph();
        assertNull(n.get(0));
        assertNull(n.get(1));
        assertEquals(0, n.get(2).size());
        assertNull(n.get(3));

        g.addVertex(0);
        n = g.exportGraph();
        assertEquals(0, n.get(0).size());
        assertNull(n.get(1));
        assertEquals(0, n.get(2).size());
        assertNull(n.get(3));

        g.addVertex(1);
        n = g.exportGraph();
        assertEquals(0, n.get(0).size());
        assertEquals(0, n.get(1).size());
        assertEquals(0, n.get(2).size());
        assertNull(n.get(-1));
        assertNull(n.get(3));
        assertNull(n.get(100));
    }

    @Test
    void addEdgeShouldIncreaseEdgeCount() {
        Graph g = new Graph();

        g.addVertex(0);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);

        g.addEdge(0, 1);
        g.addEdge(1, 0);
        assertEquals(1, g.getNumEdge());
        g.addEdge(0, 2);
        g.addEdge(2, 0);
        assertEquals(2, g.getNumEdge());
        g.addEdge(0, 3);
        g.addEdge(3, 0);
        assertEquals(3, g.getNumEdge());
        g.addEdge(1, 3);
        g.addEdge(3, 1);
        assertEquals(4, g.getNumEdge());
        g.addEdge(2, 3);
        g.addEdge(3, 2);
        assertEquals(5, g.getNumEdge());
    }

    @Test
    void addEdgeRepeatedlyShouldOnlyCountOnce() {
        Graph g = new Graph();
        Map<Integer, Set<Integer>> n;

        g.addVertex(0);
        g.addVertex(1);
        g.addVertex(2);

        g.addEdge(0, 1);
        n = g.exportGraph();
        assertTrue(n.get(0).contains(1));
        assertTrue(n.get(1).contains(0));
        assertEquals(1, g.getNumEdge());

        g.addEdge(0, 2);
        n = g.exportGraph();
        assertTrue(n.get(0).contains(2));
        assertTrue(n.get(2).contains(0));
        assertEquals(2, g.getNumEdge());

        g.addEdge(0, 1);
        n = g.exportGraph();
        assertTrue(n.get(0).contains(1));
        assertTrue(n.get(1).contains(0));
        assertEquals(2, g.getNumEdge());

        g.addEdge(1, 2);
        n = g.exportGraph();
        assertTrue(n.get(1).contains(2));
        assertTrue(n.get(2).contains(1));
        assertEquals(3, g.getNumEdge());

        g.addEdge(0, 1);
        n = g.exportGraph();
        assertTrue(n.get(0).contains(1));
        assertTrue(n.get(1).contains(0));
        assertEquals(3, g.getNumEdge());
    }

    @Test
    void addAnEdgeShouldUpdateBothNeighbors() {
        Graph g = new Graph();
        Map<Integer, Set<Integer>> n;

        g.addVertex(0);
        g.addVertex(1);
        g.addVertex(2);

        g.addEdge(0, 1);
        g.addEdge(2, 0);

        n = g.exportGraph();
        assertTrue(n.get(0).contains(1));
        assertTrue(n.get(1).contains(0));
        assertTrue(n.get(0).contains(2));
        assertTrue(n.get(2).contains(0));
    }

    @Test
    void addEdgeShouldInsertCorrectNeighbor() {
        Graph g = new Graph();

        g.addVertex(0);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);

        g.addEdge(0, 1);
        g.addEdge(1, 0);
        g.addEdge(0, 2);
        g.addEdge(2, 0);
        g.addEdge(0, 3);
        g.addEdge(3, 0);

        g.addEdge(1, 3);
        g.addEdge(3, 1);
        g.addEdge(1, 5);
        g.addEdge(5, 1);

        g.addEdge(2, 3);
        g.addEdge(3, 2);

        g.addEdge(3, 5);
        g.addEdge(5, 3);

        Set<Integer> currSet;
        Map<Integer, Set<Integer>> n = g.exportGraph();

        currSet = n.get(0);
        assertEquals(3, currSet.size());
        assertFalse(currSet.contains(0));
        assertTrue(currSet.contains(1));
        assertTrue(currSet.contains(2));
        assertTrue(currSet.contains(3));
        assertFalse(currSet.contains(4));
        assertFalse(currSet.contains(5));

        currSet = n.get(1);
        assertEquals(3, currSet.size());
        assertTrue(currSet.contains(0));
        assertFalse(currSet.contains(1));
        assertFalse(currSet.contains(2));
        assertTrue(currSet.contains(3));
        assertFalse(currSet.contains(4));
        assertTrue(currSet.contains(5));

        currSet = n.get(2);
        assertEquals(2, currSet.size());
        assertTrue(currSet.contains(0));
        assertFalse(currSet.contains(1));
        assertFalse(currSet.contains(2));
        assertTrue(currSet.contains(3));
        assertFalse(currSet.contains(4));
        assertFalse(currSet.contains(5));

        currSet = n.get(3);
        assertEquals(4, currSet.size());
        assertTrue(currSet.contains(0));
        assertTrue(currSet.contains(1));
        assertTrue(currSet.contains(2));
        assertFalse(currSet.contains(3));
        assertFalse(currSet.contains(4));
        assertTrue(currSet.contains(5));

        currSet = n.get(4);
        assertEquals(0, currSet.size());
        assertFalse(currSet.contains(0));
        assertFalse(currSet.contains(1));
        assertFalse(currSet.contains(2));
        assertFalse(currSet.contains(3));
        assertFalse(currSet.contains(4));
        assertFalse(currSet.contains(5));

        currSet = n.get(5);
        assertEquals(2, currSet.size());
        assertFalse(currSet.contains(0));
        assertTrue(currSet.contains(1));
        assertFalse(currSet.contains(2));
        assertTrue(currSet.contains(3));
        assertFalse(currSet.contains(4));
        assertFalse(currSet.contains(5));
    }
}