package dp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class FloydWarshallTest {

    private static final long INF = FloydWarshall.INF;

    // Grafo de 4 vértices con caminos conocidos
    private long[][] buildGraph4() {
        long[][] w = {
            {0,   3,   INF, 7  },
            {8,   0,   2,   INF},
            {5,   INF, 0,   1  },
            {2,   INF, INF, 0  }
        };
        return w;
    }

    // (a) Distancias conocidas en grafo de 4 vértices
    @Test
    void testKnownDistances() {
        long[][] dist = FloydWarshall.solve(buildGraph4());
        // Caminos esperados calculados a mano
        assertEquals(0,  dist[0][0]);
        assertEquals(3,  dist[0][1]);
        assertEquals(5,  dist[0][2]);
        assertEquals(6,  dist[0][3]);
        assertEquals(5,  dist[1][0]);
        assertEquals(0,  dist[1][1]);
        assertEquals(2,  dist[1][2]);
        assertEquals(3,  dist[1][3]);
        assertEquals(3,  dist[2][0]);
        assertEquals(6,  dist[2][1]);
        assertEquals(0,  dist[2][2]);
        assertEquals(1,  dist[2][3]);
        assertEquals(2,  dist[3][0]);
        assertEquals(5,  dist[3][1]);
        assertEquals(7,  dist[3][2]);
        assertEquals(0,  dist[3][3]);
    }

    // (b) Ciclo negativo lanza IllegalStateException
    @Test
    void testNegativeCycleThrows() {
        long[][] w = {
            {0,   1,   INF},
            {INF, 0,   1  },
            {-3,  INF, 0  }
        };
        assertThrows(IllegalStateException.class, () -> FloydWarshall.solve(w));
        assertTrue(FloydWarshall.hasNegativeCycle(w));
    }

    // (c) Sin arista entre dos vértices retorna INF
    @Test
    void testNoPathReturnsINF() {
        long[][] w = {
            {0,   1,   INF},
            {INF, 0,   1  },
            {INF, INF, 0  }
        };
        long[][] dist = FloydWarshall.solve(w);
        assertEquals(INF, dist[1][0]);
        assertEquals(INF, dist[2][0]);
        assertEquals(INF, dist[2][1]);
    }

    // (d) reconstructPath retorna trayectoria válida con costo == dist[s][t]
    @Test
    void testReconstructPath() {
        long[][] w = buildGraph4();
        long[][] dist = FloydWarshall.solve(w);

        int[][] pairs = {{0,3},{1,3},{2,0},{3,2}};
        for (int[] p : pairs) {
            int s = p[0], t = p[1];
            List<Integer> path = FloydWarshall.reconstructPath(w, s, t);

            // Primer y último vértice correctos
            assertEquals(s, path.get(0));
            assertEquals(t, path.get(path.size() - 1));

            // Costo del camino == dist[s][t]
            long cost = 0;
            for (int i = 0; i < path.size() - 1; i++) {
                int u = path.get(i), v = path.get(i + 1);
                assertTrue(w[u][v] < INF, "Arista inexistente en el camino: " + u + "->" + v);
                cost += w[u][v];
            }
            assertEquals(dist[s][t], cost,
                "Costo del camino != dist[" + s + "][" + t + "]");
        }
    }

    // Sin camino: reconstructPath retorna lista vacía
    @Test
    void testReconstructPathNoPath() {
        long[][] w = {
            {0,   1,   INF},
            {INF, 0,   1  },
            {INF, INF, 0  }
        };
        List<Integer> path = FloydWarshall.reconstructPath(w, 2, 0);
        assertTrue(path.isEmpty());
    }
}